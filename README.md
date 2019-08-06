## Configuration Files
- <code>/config</code> directory is where the applicatyion would read the config.txt file
- <code>/output</code> directory is where the application would output the snapshots to

## Installing and Starting Server
- install Java Version 1.8
- to run project
  - ./mvnw clean package
  - ./mvnw spring-boot:run -Drun.arguments=--node.id=0
- to run verification
  - ./mvnw spring-boot:run -Drun.arguments=--verify
