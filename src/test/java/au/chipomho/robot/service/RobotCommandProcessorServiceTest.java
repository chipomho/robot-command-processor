package au.chipomho.robot.service;

import au.chipomho.robot.exceptions.RobotOutOfBoundsException;
import au.chipomho.robot.model.CommandRequest;
import au.chipomho.robot.model.CommandType;
import au.chipomho.robot.model.DirectionType;
import au.chipomho.robot.model.PositionModel;
import au.chipomho.robot.properties.TableTopProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("Robot Command Processor Service")
@SuppressWarnings({"PMD.LongVariable"})
class RobotCommandProcessorServiceTest {

  @Mock
  private TableTopProperties tableTopProperties;

  @InjectMocks
  private RobotCommandProcessorService service;

  public RobotCommandProcessorServiceTest(){
  }

  @BeforeEach
  void setUp() {
    // Mocking the 5x5 tabletop dimensions as per requirements
    lenient().when(tableTopProperties.getWidth()).thenReturn(5);
    lenient().when(tableTopProperties.getHeight()).thenReturn(5);
  }

  @Test
  @DisplayName("Should successfully process a sequence of valid commands")
  void shouldProcessValidCommands() {
    // Example: PLACE 1,2,EAST -> MOVE -> MOVE -> LEFT -> MOVE -> REPORT [cite: 27]
    final PositionModel origin = new PositionModel().x(1).y(2).direction(DirectionType.EAST);
    final CommandRequest request = new CommandRequest();
    request.setOrigin(origin);
    request.setCommands(List.of(
        CommandType.MOVE,
        CommandType.MOVE,
        CommandType.LEFT,
        CommandType.MOVE
    ));

    final PositionModel result = service.execute(request);

    assertAll(
        () -> assertEquals(3, result.getX(), "Final X should be 3"),
        () -> assertEquals(3, result.getY(), "Final Y should be 3"),
        () -> assertEquals(DirectionType.NORTH, result.getDirection(), "Final direction should be NORTH")
    );
  }

  @Test
  @DisplayName("Should throw RobotOutOfBoundsException when a move causes the robot to fall")
  void shouldThrowExceptionWhenRobotFalls() {
    // Positioned at NORTH edge (0,4) facing NORTH
    final PositionModel origin = new PositionModel().x(0).y(4).direction(DirectionType.NORTH);
    final CommandRequest request = new CommandRequest();
    request.setOrigin(origin);
    request.setCommands(List.of(CommandType.MOVE));

    // Validation: Movement resulting in falling must be prevented [cite: 5, 22]
    Assertions.assertThrows(RobotOutOfBoundsException.class, () -> service.execute(request));
  }

  @Test
  @DisplayName("Should return origin if commands list is empty")
  void shouldReturnOriginWhenCommandsAreNull() {
    final PositionModel origin = new PositionModel().x(0).y(0).direction(DirectionType.NORTH);
    final CommandRequest request = new CommandRequest();
    request.setOrigin(origin);
    request.setCommands(new ArrayList<>());

    final PositionModel result = service.execute(request);

    assertEquals(origin, result, "Should return the origin if no commands are provided");
  }

}