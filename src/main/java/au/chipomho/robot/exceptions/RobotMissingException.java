package au.chipomho.robot.exceptions;

import java.io.Serial;

@SuppressWarnings({"PMD.NonSerializableClass"})
public class RobotMissingException  extends RuntimeException  {
  @Serial
  private static final long serialVersionUID = 8828463977305899422L;

  public RobotMissingException(final String message) {
    super(message);
  }

}
