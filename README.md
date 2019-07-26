## Configuration Files
- /config directory is where the applicatyion would read the config.txt file
- /output directory is where the application would output the snapshots to

## Installing and Starting Server
- install Java Version 1.8
- if maven binary is installed
  - mvn clean package
  - mvn spring-boot:run
  - mvn spring-boot:run -Drun.arguments=--node.id=0
- if no maven binary is installed, use the maven wrapper provided
  - ./mvnw spring-boot:run -Drun.arguments=--node.id=0
  - the command above will both:
    - download the dependent java libraries
    - then start the application