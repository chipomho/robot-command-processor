package au.chipomho.robot.controller;

import au.chipomho.robot.RobotCommandProcessorApplication;
import au.chipomho.robot.model.CommandRequest;
import au.chipomho.robot.model.CommandType;
import au.chipomho.robot.model.DirectionType;
import au.chipomho.robot.model.PositionModel;
import au.chipomho.robot.properties.TableTopProperties;
import au.chipomho.robot.service.RobotCommandProcessorService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RobotCommandProcessorController.class)
@ContextConfiguration(classes = RobotCommandProcessorApplication.class)
@DisplayName("Robot Command Processor Controller")
@SuppressWarnings({"PMD.TooManyStaticImports","PMD.AtLeastOneConstructor",
    "PMD.LongVariable","PMD.JUnitTestsShouldIncludeAssert"})
class RobotCommandProcessorControllerTest {

  public static final String REQUEST_PATH_EXECUTE = "/execute";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private RobotCommandProcessorService service;

  @Autowired
  private TableTopProperties tableTopProperties;

  @BeforeEach
  void setUp() {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Test
  @DisplayName("GET /health should return 200 OK")
  @Order(1)
  @Timeout(5)
  void healthStatusTest() throws Exception {
    mockMvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("POST /execute should return processed position")
  @Order(2)
  @Timeout(5)
  void shouldReturnProcessedPosition() throws Exception {
    final PositionModel mockResponse = new PositionModel().x(0).y(1).direction(DirectionType.NORTH);
    when(service.execute(any(CommandRequest.class))).thenReturn(mockResponse);

    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(0).y(0).direction(DirectionType.NORTH));
    request.setCommands(List.of());

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.y").value(1))
        .andExpect(jsonPath("$.direction").value("NORTH"));
  }

  @Test
  @DisplayName("POST /execute - Success with empty commands")
  @Order(3)
  @Timeout(5)
  void shouldReturnSuccessEvenWhenCommandsAreEmpty() throws Exception {
    final PositionModel response = new PositionModel().x(0).y(0).direction(DirectionType.NORTH);
    when(service.execute(any())).thenReturn(response);

    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(0).y(0).direction(DirectionType.NORTH));
    request.setCommands(List.of());

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.x").value(0));
  }

  @Test
  @DisplayName("POST /execute - Success with MOVE sequence")
  @Order(4)
  @Timeout(5)
  void shouldSuccessfullyMoveRobot() throws Exception {
    final PositionModel response = new PositionModel().x(0).y(1).direction(DirectionType.NORTH);
    when(service.execute(any())).thenReturn(response);

    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(0).y(0).direction(DirectionType.NORTH));
    request.setCommands(List.of(CommandType.MOVE));

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.y").value(1));
  }

  @Test
  @DisplayName("POST /execute - Fail if Origin X is negative")
  @Order(5)
  @Timeout(5)
  void shouldFailIfPlacementOfRobotIsInvalidX() throws Exception {
    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(-1).y(0).direction(DirectionType.NORTH));

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("POST /execute - Fail if Origin Y is negative")
  @Order(6)
  @Timeout(5)
  void shouldFailIfPlacementOfRobotIsInavlidY() throws Exception {
    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(0).y(-1).direction(DirectionType.NORTH));

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("POST /execute - Fail if Origin is null (Robot Missing)")
  @Order(7)
  @Timeout(5)
  void shouldFailIfRobotPlacementIsMissing() throws Exception {
    final CommandRequest request = new CommandRequest();
    request.setOrigin(null);

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("POST /execute - Fail if Direction is null")
  @Order(8)
  @Timeout(5)
  void shouldFailIfDirectionIsMissing() throws Exception {
    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(0).y(0).direction(null));

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("POST /execute - Handle Invalid JSON structure")
  @Order(9)
  @Timeout(5)
  void shouldHandleInvalidJsonData() throws Exception {
    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"origin\": \"invalid\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("POST /execute - Success at Max Boundary (4,4)")
  @Order(10)
  @Timeout(5)
  void shouldBeOkAtMaximumBoundary() throws Exception {
    final PositionModel response = new PositionModel().x(4).y(4).direction(DirectionType.NORTH);
    when(service.execute(any())).thenReturn(response);

    final CommandRequest request = new CommandRequest();
    request.setOrigin(new PositionModel().x(4).y(4).direction(DirectionType.NORTH));
    request.setCommands(List.of(CommandType.LEFT));

    mockMvc.perform(post(REQUEST_PATH_EXECUTE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }
}