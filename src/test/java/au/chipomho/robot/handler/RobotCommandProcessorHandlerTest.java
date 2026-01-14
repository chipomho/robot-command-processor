package au.chipomho.robot.handler;

import au.chipomho.robot.model.CommandType;
import au.chipomho.robot.model.DirectionType;
import au.chipomho.robot.model.PositionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Robot Command Processor Handler")
@SuppressWarnings({"PMD.VisibilityModifier","PMD.AtLeastOneConstructor", "PMD.JUnitTestContainsTooManyAsserts"})
class RobotCommandProcessorHandlerTest {

  protected static final int MAX_X = 5;
  protected static final int MAX_Y = 5;


  /**
   * Position Model for Origin
   */
  private PositionModel origin;
  /**
   * Class instance
   */
  private RobotCommandProcessorHandler handler;

  @BeforeEach
  void setUp() {
    origin = new PositionModel(0,0,DirectionType.EAST);
    handler = new RobotCommandProcessorHandler(origin, MAX_X, MAX_Y);
  }

  /**
   * Static provider for the test.
   * Creates handlers positioned at the boundaries of the 5x5 table.
   */
  private static Stream<RobotCommandProcessorHandler> robotOnEdge() {
    return Stream.of(
        new RobotCommandProcessorHandler(new PositionModel().x(0).y(0).direction(DirectionType.SOUTH),5,5),
        new RobotCommandProcessorHandler(new PositionModel().x(0).y(0).direction(DirectionType.WEST),5,5),
        new RobotCommandProcessorHandler(new PositionModel().x(0).y(4).direction(DirectionType.NORTH),5,5),
        new RobotCommandProcessorHandler(new PositionModel().x(4).y(0).direction(DirectionType.EAST),5,5),
        new RobotCommandProcessorHandler(new PositionModel().x(4).y(4).direction(DirectionType.NORTH),5,5),
        new RobotCommandProcessorHandler(new PositionModel().x(4).y(4).direction(DirectionType.EAST),5,5)
    );
  }

  private static Stream<RobotPositionModel> robotAtOriginTurnLeft() {
    return Stream.of(
        new RobotPositionModel(DirectionType.EAST, DirectionType.NORTH),
        new RobotPositionModel(DirectionType.NORTH, DirectionType.WEST),
        new RobotPositionModel(DirectionType.WEST, DirectionType.SOUTH),
        new RobotPositionModel(DirectionType.SOUTH, DirectionType.EAST)
        );
  }

  private static Stream<RobotPositionModel> robotAtOriginTurnRight() {
    return Stream.of(
        new RobotPositionModel(DirectionType.EAST, DirectionType.SOUTH),
        new RobotPositionModel(DirectionType.NORTH, DirectionType.EAST),
        new RobotPositionModel(DirectionType.WEST, DirectionType.NORTH),
        new RobotPositionModel(DirectionType.SOUTH, DirectionType.WEST)
    );
  }

  @Test
  @DisplayName("Should successfully move when facing EAST")
  @Timeout(10)
  @Order(0)
  void shouldSuccessfullyMoveToRightWhenFacingEast() {
    //setup
    origin.setX(0);
    origin.setY(0);
    origin.setDirection(DirectionType.EAST);
    handler = new RobotCommandProcessorHandler(origin, MAX_X, MAX_Y);
    //execute
    handler.execute(CommandType.MOVE);
    //verify
    assertEquals(handler.getCurrentPosition().getX(), origin.getX() + 1, "Should have moved to the right");
    assertEquals(handler.getCurrentPosition().getY(), origin.getY(), "Should have no change on Y");
  }

  @Test
  @DisplayName("Should successfully move when facing WEST")
  @Timeout(10)
  @Order(1)
  void shouldSuccessfullyMoveToLeftWhenFacingWest() {
    //setup
    origin.setX(4);
    origin.setY(0);
    origin.setDirection(DirectionType.WEST);
    handler = new RobotCommandProcessorHandler(origin, MAX_X, MAX_Y);
    //execute
    handler.execute(CommandType.MOVE);
    //verify
    assertEquals(handler.getCurrentPosition().getX(), origin.getX() - 1, "Should have moved to the left");
    assertEquals(handler.getCurrentPosition().getY(), origin.getY(), "Should not have moved Y");
  }

  @Test
  @DisplayName("Should successfully move to the left when facing NORTH")
  @Timeout(10)
  @Order(2)
  void shouldSuccessfullyMoveToLeftWhenFacingNorth() {
    //setup
    origin.setX(0);
    origin.setY(0);
    origin.setDirection(DirectionType.NORTH);
    handler = new RobotCommandProcessorHandler(origin, MAX_X, MAX_Y);
    //execute
    handler.execute(CommandType.MOVE);
    //verify
    assertEquals(handler.getCurrentPosition().getX(), origin.getX(), "Should have not change X");
    assertEquals(handler.getCurrentPosition().getY(), origin.getY() + 1, "Should have moved to the North");
  }

  @Test
  @DisplayName("Should successfully move to the left when facing SOUTH")
  @Timeout(10)
  @Order(3)
  void shouldSuccessfullyMoveToLeftWhenFacingSouth() {
    //setup
    origin.setX(0);
    origin.setY(4);
    origin.setDirection(DirectionType.SOUTH);
    handler = new RobotCommandProcessorHandler(origin, MAX_X, MAX_Y);
    //execute
    handler.execute(CommandType.MOVE);
    //verify
    assertEquals(handler.getCurrentPosition().getX(), origin.getX(), "Should have not change X");
    assertEquals(handler.getCurrentPosition().getY(), origin.getY() - 1, "Should have moved to the South");

  }

  @DisplayName("Should fail to move if on the edge")
  @Timeout(10)
  @Order(4)
  @ParameterizedTest
  @MethodSource("robotOnEdge")
  void shouldFailToMoveIfOnTheEdgeOrigin(final RobotCommandProcessorHandler param) {
    //verify
    assertFalse(param.canExecute(CommandType.MOVE), "Should return false cant move when at the edge");
  }

  @DisplayName("Should should be able to turn in all directions when turning left")
  @Timeout(10)
  @Order(5)
  @ParameterizedTest
  @MethodSource("robotAtOriginTurnLeft")
  void shouldBeAbleToTurnInAllDirectionsWhenTurningLeft(final RobotPositionModel param) {
    //verify
    final RobotCommandProcessorHandler handler =
        new RobotCommandProcessorHandler(new PositionModel().x(0).y(0).direction(param.initial), MAX_X, MAX_Y);
    handler.execute(CommandType.LEFT);
    assertEquals(0,handler.getCurrentPosition().getX(), "Should remain at the same X location");
    assertEquals(0,handler.getCurrentPosition().getY(), "Should remain at the same Y location");
    assertEquals(param.destination,handler.getCurrentPosition().getDirection(), "Should be facing: "+ param.destination);
  }

  @DisplayName("Should should be able to turn in all directions when turning Right")
  @Timeout(10)
  @Order(6)
  @ParameterizedTest
  @MethodSource("robotAtOriginTurnRight")
  void shouldBeAbleToTurnInAllDirectionsWhenTurningRight(final RobotPositionModel param) {
    //verify
    final RobotCommandProcessorHandler handler =
        new RobotCommandProcessorHandler(new PositionModel().x(0).y(0).direction(param.initial), MAX_X, MAX_Y);
    handler.execute(CommandType.RIGHT);
    assertEquals(0,handler.getCurrentPosition().getX(), "Should remain at the same location");
    assertEquals(0,handler.getCurrentPosition().getY(), "Should remain at the same location");
    assertEquals(param.destination,handler.getCurrentPosition().getDirection(), "Should be facing: "+ param.destination);
  }

  private record RobotPositionModel(DirectionType initial, DirectionType destination) {

    @Override
    public String toString() {
      return String.format("Turn (%s => %s)", initial.toString(), destination.toString());
    }
  }

}