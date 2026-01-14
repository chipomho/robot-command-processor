package au.chipomho.robot.advice;


import au.chipomho.robot.exceptions.RobotMissingException;
import au.chipomho.robot.exceptions.RobotOutOfBoundsException;
import au.chipomho.robot.model.ApiErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
@Slf4j
@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.LongVariable"})
public class GlobalExceptionHandler {

  public static final String OUT_OF_BOUNDS = "OUT_OF_BOUNDS";
  public static final String ROBOT_MISSING = "ROBOT_MISSING";
  public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
  public static final String INVALID_INPUT_DATA = "INVALID_INPUT_DATA";

  @ExceptionHandler(RobotOutOfBoundsException.class)
  public ResponseEntity<Object> handleRobotOutOfBounds(final RobotOutOfBoundsException exception) {
    if (log.isErrorEnabled()) {
      log.error("Robot Out of Bounds: {}", exception.getMessage(), exception);
    }
    return new ResponseEntity<>(ApiErrorModel.builder().error(OUT_OF_BOUNDS)
        .message(exception.getMessage()).position(exception.getPosition()).build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RobotMissingException.class)
  public ResponseEntity<Object> handleRobotMissing(final RobotMissingException exception) {
    if (log.isErrorEnabled()) {
      log.error("Robot Missing: {}", exception.getMessage(), exception);
    }
    return new ResponseEntity<>(ApiErrorModel.builder().error(ROBOT_MISSING).message(exception.getMessage()).build(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGeneralError(final Exception exception) {
    if (log.isErrorEnabled()) {
      log.error("Trouble processing request: {}", exception.getMessage(), exception);
    }
    //error friendly message
    return new ResponseEntity<>(ApiErrorModel.builder().error(INTERNAL_SERVER_ERROR)
        .message("Problem processing request").build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
    if (log.isErrorEnabled()) {
      log.error("Trouble processing request: {}", exception.getMessage(), exception);
    }
    //error friendly message
    return new ResponseEntity<>(ApiErrorModel.builder().error(INVALID_INPUT_DATA)
        .message("Invalid Input Request").build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(final HttpMessageNotReadableException exception) {
    if (log.isErrorEnabled()) {
      log.error("Trouble processing request: {}", exception.getMessage(), exception);
    }
    //error friendly message
    return new ResponseEntity<>(ApiErrorModel.builder().error(INVALID_INPUT_DATA)
        .message("Invalid Input Request").build(), HttpStatus.BAD_REQUEST);
  }

}
