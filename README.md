# testtask-sectors-backend
This is a RESTful API application for managing search filters. It does not represent any business value and is simply a personal project.

### Prerequisites

Before you can run this application, you need to have the following installed:

*   [Java 21] (or higher)

### How to Run Your Application

1) Set Java 21 as JDK in Project Structure -> Project Settings -> Project
2) Choose Distribution=Wrapper in Settings -> Build, Execution, Deployment -> Build tools -> Gradle 
3) Download Gradle and dependencies ```./gradlew assemble```
4) Run Spring-boot application with ```./gradlew bootRun``` or by executing main method in TesttaskFiltersApplication.java

To test the application, you can also use [swagger](http://localhost:8080/swagger-ui/index.html)
