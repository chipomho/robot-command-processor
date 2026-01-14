package au.chipomho.robot.controller;

import au.chipomho.robot.annotations.Monitored;
import au.chipomho.robot.api.RobotCommandProcessorApi;
import au.chipomho.robot.model.CommandRequest;
import au.chipomho.robot.model.PositionModel;
import au.chipomho.robot.model.ReleaseModel;
import au.chipomho.robot.properties.TableTopProperties;
import au.chipomho.robot.service.RobotCommandProcessorService;
import au.chipomho.robot.validator.CommandRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Monitored()
public class RobotCommandProcessorController implements RobotCommandProcessorApi {

  private final TableTopProperties tableTop;
  private final RobotCommandProcessorService service;

  @Override
  public ResponseEntity<PositionModel> executeRobotCommands(final CommandRequest commandRequest) {
    CommandRequestValidator.validate(commandRequest, tableTop);
    return ResponseEntity.ok(service.execute(commandRequest));
  }

  @Override
  public ResponseEntity<ReleaseModel> getHealthStatus() {
    return ResponseEntity.ok(new ReleaseModel());
  }
}
