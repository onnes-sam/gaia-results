#Gaia results

API for showing ADQL results

#Getting Started

Launch http://localhost:8082/
Upload csv and click "Import CSV"

##Technology Stack:
* Java 11
* Maven
* Spring Boot

##Tool Prerequisites:
* JDK 11 (https://sdkman.io/)
* IntelliJ IDE (https://www.jetbrains.com/idea/)

# Importing to IntelliJ
Perform the following Steps:
* `File` > `New` > `Project from Existing Sources...`
* Select the root folder of this project
* Select `Import project from external model`
* Highlight `Maven` and click `Finish`

# Build
To build:
```
./mvnw clean install
```
## Run from IntelliJ
* Right click gaia-results -> Run/Debug

## Run from Maven
```
 mvn spring-boot:run -pl gaia-results
```

## Build 'fat jar' and run
```
mvn clean install
java -jar gaia-results/target/gaia-results.jar