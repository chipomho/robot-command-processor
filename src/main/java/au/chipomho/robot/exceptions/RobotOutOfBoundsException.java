package au.chipomho.robot.exceptions;

import au.chipomho.robot.model.PositionModel;
import lombok.Getter;

import java.io.Serial;

@Getter
@SuppressWarnings({"PMD.NonSerializableClass"})
public class RobotOutOfBoundsException extends RuntimeException  {

  @Serial
  private static final long serialVersionUID = 7010900505427187621L;

  private PositionModel position;

  public RobotOutOfBoundsException(final String message) {
    super(message);
  }

  public RobotOutOfBoundsException(final String message, final PositionModel origin) {
    super(message);
    this.position = origin;
  }

}
