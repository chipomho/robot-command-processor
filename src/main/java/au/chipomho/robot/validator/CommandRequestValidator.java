package au.chipomho.robot.validator;

import au.chipomho.robot.exceptions.RobotMissingException;
import au.chipomho.robot.exceptions.RobotOutOfBoundsException;
import au.chipomho.robot.model.CommandRequest;
import au.chipomho.robot.model.PositionModel;
import au.chipomho.robot.properties.TableTopProperties;

import java.util.Objects;

@SuppressWarnings({"PMD.LongVariable"})
public class CommandRequestValidator {


  public static final String ROBOT_MISSING = "Robot is missing";
  public static final String ROBOT_OUT_OF_BOUNDS = "Robot out of bounds";

  public static void validate(final CommandRequest commandRequest, final TableTopProperties tableTopProperties) {
    //well check the boundaries
    final PositionModel origin = commandRequest.getOrigin();

    if (Objects.isNull(origin)){
       throw new RobotMissingException(ROBOT_MISSING);
    }

    if (tableTopProperties.getWidth() < origin.getX() || tableTopProperties.getHeight() < origin.getY() || origin.getY()<0 || origin.getX()<0) {
      throw new RobotOutOfBoundsException(ROBOT_OUT_OF_BOUNDS);
    }
  }

}
