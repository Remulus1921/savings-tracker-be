# savings-tracker-be

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21**
- **Docker**
- **Gradle** (optional, as the project includes the Gradle wrapper)

## Configuring Environment Variables

### Linux & macOS
Before starting the application, configure the required environment variables:

```bash
export JWT_SECRET_KEY=your_secret_key
export SPRING_PROFILES_ACTIVE=local
```

Alternatively, you can specify the active profile at runtime:

```bash
java -Dspring.profiles.active=local -jar app.jar
```

### Windows (Command Prompt)
```cmd
set JWT_SECRET_KEY=your_secret_key
set SPRING_PROFILES_ACTIVE=local
```

Alternatively, you can specify the active profile at runtime:

```cmd
java -Dspring.profiles.active=local -jar app.jar
```

### Windows (PowerShell)
```powershell
$env:JWT_SECRET_KEY="your_secret_key"
$env:SPRING_PROFILES_ACTIVE="local"
```

Alternatively, you can specify the active profile at runtime:

```powershell
java -Dspring.profiles.active=local -jar app.jar
```
## Installation

To download dependencies and build the project, run:

```bash
./gradlew build
```

## Running the Application

### Option 1: Running with Docker

Ensure Docker is running, then execute:

```bash
docker-compose up
```

### Option 2: Running Locally

To start the application without Docker, use one of the following commands:

```bash
./gradlew bootRun
```

Or run the compiled JAR file directly:

```bash
java -jar build/libs/app.jar
```

## Additional Notes

- If running via IntelliJ IDEA, make sure to set `SPRING_PROFILES_ACTIVE=local` in the run configurations.
- Ensure that `docker-compose.yml` contains the necessary services for the application to function properly.
- Logs and errors can be monitored in the terminal while running the application.

---

## License

This project is licensed under the Apache License 2.0. For details, see the LICENSE.txt file.

