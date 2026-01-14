package au.chipomho.robot.handler;

import au.chipomho.robot.model.CommandType;
import au.chipomho.robot.model.DirectionType;
import au.chipomho.robot.model.PositionModel;
import lombok.Getter;


@Getter()
public class RobotCommandProcessorHandler {
  private final PositionModel origin;
  private final int maxX;
  private final int maxY;
  private final PositionModel currentPosition;

  public RobotCommandProcessorHandler(final PositionModel origin, final int maxX, final int maxY) {
    this.origin = origin;
    this.maxX = maxX;
    this.maxY = maxY;
    currentPosition = new PositionModel(origin.getX(), origin.getY(), origin.getDirection());
  }


  public boolean canExecute(final CommandType command) {
    return switch (command) {
      case MOVE -> canMove();
      case RIGHT,LEFT -> true;
    };
  }

  public void execute(final CommandType command) {
    switch (command) {
      case MOVE -> move();
      case LEFT -> turnLeft();
      case RIGHT -> turnRight();
    }
  }

  protected void move() {
    switch (currentPosition.getDirection()){
      case EAST -> currentPosition.setX(currentPosition.getX() + 1);
      case WEST -> currentPosition.setX(currentPosition.getX() - 1);
      case NORTH -> currentPosition.setY(currentPosition.getY() + 1);
      case SOUTH -> currentPosition.setY(currentPosition.getY() - 1);
    }
  }

  protected void turnLeft() {
    switch (currentPosition.getDirection()){
      case EAST -> currentPosition.setDirection(DirectionType.NORTH);
      case WEST -> currentPosition.setDirection(DirectionType.SOUTH);
      case NORTH -> currentPosition.setDirection(DirectionType.WEST);
      case SOUTH -> currentPosition.setDirection(DirectionType.EAST);
    }
  }

  protected void turnRight() {
    switch (currentPosition.getDirection()){
      case EAST -> currentPosition.setDirection(DirectionType.SOUTH);
      case WEST -> currentPosition.setDirection(DirectionType.NORTH);
      case NORTH -> currentPosition.setDirection(DirectionType.EAST);
      case SOUTH -> currentPosition.setDirection(DirectionType.WEST);
    }
  }

  protected boolean canMove() {
    return switch (currentPosition.getDirection()) {
      case EAST -> currentPosition.getX() + 1 < maxX;
      case WEST -> currentPosition.getX() - 1 >= 0;
      case NORTH -> currentPosition.getY() + 1 < maxY;
      case SOUTH -> currentPosition.getY() - 1 >= 0;
    };
  }


}
