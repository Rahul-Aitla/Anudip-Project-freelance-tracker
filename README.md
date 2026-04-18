# Freelancer Project Tracker

Freelancer Project Tracker is a Java web application for managing clients, projects, payments, and tasks.

## Tech Stack
- Java 17
- Servlet + JSP (JSTL)
- JDBC
- MySQL
- Maven

## Features
- User authentication: register, login, logout
- Dashboard with key project and payment summary
- Client management
- Project management
- Payment tracking
- Task tracking for each project

## Prerequisites
- JDK 17+
- MySQL running on port 3306
- Maven

If Maven is not available in PATH, use the bundled Maven from this workspace:
- `D:\anudip\apache-maven-3.9.11\bin\mvn.cmd`

## Database Setup
1. Create the database and tables by running:

```powershell
"C:\Program Files\MySQL\MySQL Server 9.5\bin\mysql.exe" -u root -proot -e "CREATE DATABASE IF NOT EXISTS freelancer_tracker;"
Get-Content -Raw .\sql\schema.sql | "C:\Program Files\MySQL\MySQL Server 9.5\bin\mysql.exe" -u root -proot freelancer_tracker
```

2. Default application DB config is in `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://127.0.0.1:3306/freelancer_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=tracker
db.password=tracker123
```

3. Optional environment variable overrides:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Build
From `D:\anudip\freelancer-tracker`:

```powershell
D:\anudip\apache-maven-3.9.11\bin\mvn.cmd clean package
```

## Run (Embedded Jetty)
From `D:\anudip`:

```powershell
D:\anudip\apache-maven-3.9.11\bin\mvn.cmd -f .\freelancer-tracker\pom.xml org.eclipse.jetty:jetty-maven-plugin:9.4.54.v20240208:run-war
```

Open:
- http://localhost:8080/login

## Stop
- In the same terminal where Jetty is running, press `Ctrl + C`.
- If needed, force-stop Java process:

```powershell
Get-CimInstance Win32_Process | Where-Object { $_.Name -eq "java.exe" -and $_.CommandLine -match "jetty|freelancer-tracker" } | Select-Object ProcessId,CommandLine
Stop-Process -Id <PID> -Force
```

## Cleanup Generated Files
To remove build artifacts:

```powershell
D:\anudip\apache-maven-3.9.11\bin\mvn.cmd -f .\freelancer-tracker\pom.xml clean
```

Or delete the build output folder manually:
- `freelancer-tracker\target`

## Project Structure
- `src/main/java`: Java source code (servlets, DAOs, models, utils)
- `src/main/webapp`: JSP views and static assets
- `src/main/resources`: configuration (`db.properties`)
- `sql/schema.sql`: database schema
- `pom.xml`: Maven build file
