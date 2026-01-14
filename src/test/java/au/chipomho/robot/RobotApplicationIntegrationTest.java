package au.chipomho.robot;

import au.chipomho.robot.model.ApiErrorModel;
import au.chipomho.robot.model.CommandRequest;
import au.chipomho.robot.model.CommandType;
import au.chipomho.robot.model.DirectionType;
import au.chipomho.robot.model.PositionModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {RobotCommandProcessorApplication.class})
@DisplayName("Robot Application Integration Test")
@SuppressWarnings({"PMD.AtLeastOneConstructor","PMD.ShortVariable", "PMD.JUnitTestContainsTooManyAsserts",
"PMD.JUnitAssertionsShouldIncludeMessage"})
class RobotApplicationIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  private ResponseEntity<PositionModel> send(final int x, final int y, final DirectionType dir, final List<CommandType> commands) {
    return send(x, y, dir, commands, PositionModel.class);
  }

  private <R> ResponseEntity<R> send(final int x, final int y, final DirectionType dir, final List<CommandType> commands, final Class<R> clazz) {
    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(x).y(y).direction(dir));
    request.setCommands(commands);
    return restTemplate.postForEntity("/execute", request, clazz);
  }

  @Test
  @DisplayName("Should do Standard Movement: PLACE 1,2,EAST  MOVE MOVE LEFT MOVE")
  @Order(0)
  void shouldDoStandardMovement() {
    final ResponseEntity<PositionModel> response = send(1, 2, DirectionType.EAST,
        List.of(CommandType.MOVE, CommandType.MOVE, CommandType.LEFT, CommandType.MOVE));

    assertEquals(3, response.getBody().getX());
    assertEquals(3, response.getBody().getY());
    assertEquals(DirectionType.NORTH, response.getBody().getDirection());
  }

  @Test
  @DisplayName("Should do Edge Prevention North: Try to move past Y=4")
  @Order(1)
  void shouldDoEdgePreventionNorth() {
    final ResponseEntity<ApiErrorModel> response = send(0, 4, DirectionType.NORTH, List.of(CommandType.MOVE), ApiErrorModel.class);
    final PositionModel position = response.getBody().getPosition();
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(position);
    assertEquals(4, position.getY());
  }

  @Test
  @DisplayName("Should do Edge Prevention South: Try to move past Y=0")
  @Order(2)
  void shouldDoEdgePreventionSouth() {
    final ResponseEntity<ApiErrorModel> response = send(0, 0, DirectionType.SOUTH, List.of(CommandType.MOVE), ApiErrorModel.class);
    final PositionModel position = response.getBody().getPosition();
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(position);
    assertEquals(0, position.getY());
  }

  @Test
  @DisplayName("Should do Edge Prevention East: Try to move past X=4")
  @Order(3)
  void shouldDoEdgePreventionEast() {
    final ResponseEntity<ApiErrorModel> response = send(4, 2, DirectionType.EAST, List.of(CommandType.MOVE), ApiErrorModel.class);
    final PositionModel position = response.getBody().getPosition();
    assertNotNull(position);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(4, position.getX());
  }

  @Test
  @DisplayName("Should do Edge Prevention West: Try to move past X=0")
  @Order(4)
  void shouldDoEdgePreventionWest() {
    final ResponseEntity<ApiErrorModel> response = send(0, 2, DirectionType.WEST, List.of(CommandType.MOVE), ApiErrorModel.class);
    final PositionModel position = response.getBody().getPosition();
    assertNotNull(position);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(0, position.getX());
  }

  @Test
  @DisplayName("Should do a complete rotation Left: 360 degrees")
  @Order(5)
  void shouldDoCompleteRotation() {
    final ResponseEntity<PositionModel> response = send(2, 2, DirectionType.NORTH,
        List.of(CommandType.LEFT, CommandType.LEFT, CommandType.LEFT, CommandType.LEFT));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(DirectionType.NORTH, response.getBody().getDirection());
  }

  @Test
  @DisplayName("Should do a diagonal perimeter: Move along x and y edges")
  @Order(6)
  void shouldDoDiagonalPerimeterMoveAlongXAndY() {
    final ResponseEntity<PositionModel> response = send(0, 0, DirectionType.NORTH,
        List.of(CommandType.MOVE, CommandType.MOVE, CommandType.MOVE, CommandType.MOVE,
            CommandType.RIGHT,
            CommandType.MOVE, CommandType.MOVE, CommandType.MOVE, CommandType.MOVE));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(4, response.getBody().getX());
    assertEquals(4, response.getBody().getY());
  }

  @Test
  @DisplayName("Should handle Invalid Initial Placement")
  @Order(8)
  void shouldHandleInvalidInitialPlacement() {
    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(8).y(0).direction(DirectionType.NORTH));
    final ResponseEntity<ApiErrorModel> response = restTemplate.postForEntity("/execute", request, ApiErrorModel.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }


}
