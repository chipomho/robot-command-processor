package au.chipomho.robot.service;

import au.chipomho.robot.annotations.Monitored;
import au.chipomho.robot.exceptions.RobotOutOfBoundsException;
import au.chipomho.robot.handler.RobotCommandProcessorHandler;
import au.chipomho.robot.model.CommandRequest;
import au.chipomho.robot.model.CommandType;
import au.chipomho.robot.model.PositionModel;
import au.chipomho.robot.properties.TableTopProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RobotCommandProcessorService {


  private final TableTopProperties tableTop;

  @Monitored()
  public PositionModel execute(final CommandRequest commandRequest){
    final PositionModel origin = commandRequest.getOrigin();
    //now execute commands making sure the robot wont fall of the table
    final List<CommandType> commands = commandRequest.getCommands();
    PositionModel response = origin;
    if (Objects.nonNull(commands)){
      final RobotCommandProcessorHandler handler = new RobotCommandProcessorHandler(origin, tableTop.getWidth(), tableTop.getHeight());
      int index=0;
      for(final CommandType cmd : commands){
        //check if we can execute the command
        if (!handler.canExecute(cmd)){
          throw new RobotOutOfBoundsException(String.format("Command '%s' at index(%d) is not executable", cmd, index), handler.getCurrentPosition());
        }
        index++;
        //good we are ready to execute
        handler.execute(cmd);
      }
      response = handler.getCurrentPosition();
    }
    return response;
  }
}
