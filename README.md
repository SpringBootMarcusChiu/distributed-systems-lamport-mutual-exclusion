## Configuration Files
- <code>/config</code> directory is where the applicatyion would read the config.txt file
- <code>/output</code> directory is where the application would output the snapshots to

## Installing and Starting Server
- install Java Version 1.8
- to install dependencies
  - ./mvnw clean package
- to run project
  - ./mvnw spring-boot:run -Drun.arguments=--node.id=0
  - ./mvnw spring-boot:run -Drun.arguments=--node.id=1
  - ...
- to run verification
  - ./mvnw spring-boot:run -Drun.arguments=--verify
