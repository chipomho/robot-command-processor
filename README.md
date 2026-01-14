# Truck Robot Command Processor
A robust Spring Boot simulation of a robotic vehicle navigating a nxn tabletop grid with real-time boundary validation and interactive OpenAPI documentation.

---

## Table of Contents
* [1. Project Overview](#1-project-overview)
* [2. Coordinate System & Mathematics](#2-coordinate-system--mathematics)
* [3. Command Specification](#3-command-specification)
* [4. Technical Architecture](#4-technical-architecture)
* [5. API Documentation](#5-api-documentation)
* [6. Setup & Execution](#6-setup--execution)
* [7. Testing Suite](#7-testing-suite)
* [8. Troubleshooting](#8-troubleshooting)

---

## 1. Project Overview
The **Truck Robot Command Processor** is a backend service that simulates the movement of a robot on a square tabletop. It ensures that the robot remains on the table at all times, ignoring any command that would cause it to fall.

### Key Features
- **Boundary Protection**: Robust boundary protection with responsible error handling.
- **Responsive**: The service is fast and responsive
- **Stateless**: The position os the robot is determined during a single request and must be set.

---

## 2. Coordinate System & Mathematics
The tabletop is modeled as a Cartesian plane where the South West corner is the origin.

- **Dimensions**: Defult dimensions are $5 \times 5$ units. these can be changed in $application.yaml$
- **X-Axis**: $0 \dots n$ (West to East).
- **Y-Axis**: $0 \dots n$ (South to North).
- **Movement Vectors**:
    - `NORTH`: $(0, 1)$
    - `SOUTH`: $(0, -1)$
    - `EAST`: $(1, 0)$
    - `WEST`: $(-1, 0)$

---

## 3. Command Specification
Commands are processed through a single REST endpoint: `POST /execute`.

| Command | Arguments | Description |
| :--- | :--- | :--- |
| **PLACE** | `X,Y,F` | Initializes position and direction (e.g., `PLACE 0,0,NORTH`). |
| **MOVE** | None | Advances 1 unit forward in the current direction. |
| **LEFT** | None | Rotates 90 degrees counter-clockwise. |
| **RIGHT** | None | Rotates 90 degrees clockwise. |
| **REPORT** | None | Returns the current state string (e.g., `3,3,NORTH`). |

> **Note**: Any command issued before a valid `PLACE` command will be silently ignored.

---

## 4. Technical Architecture
The project is built on **Spring Boot 3** and follows a clean service-oriented architecture.

### Logic Flow
1. **Controller**: Receives the raw command string.
2. **Parser**: Interprets the string and validates the command type.
3. **Service**: Updates the Robot state after performing a boundary check.
4. **Safety Check**: New coordinates $P'$ are validated such that $0 \le X' \le 4$ and $0 \le Y' \le 4$.

---

## 5. API Documentation
The API uses a static `openapi.yaml` file to ensure documentation remains decoupled from the code.

### Swagger UI Configuration
Because Spring Boot 3 has strict static resource handling, we use a custom `WebConfiguration` to serve the Swagger UI assets:

- **Swagger URL**: `http://localhost:8080/swagger-ui/index.html`  if the Robot Command Processor doesn't load in the browser simply type $/openapi.yaml$ in the explore search field and click $Explore$ button.

---
## 6. Setup & Execution

### Prerequisites
- JDK 17+
- Gradle 8.x

### Build & Run
```bash
# Build the project
./gradlew clean build

# Run the application
./gradlew bootRun