** Version 0.0.1**


# item-eligility-checker
Demonstrates a RESTful web service using Spring Boot and Java. This webservice provides ability for an ecomm based organization to check if the product is eligible for a new shipping program, the eligiblity is determined by a set of preconfigured business rules. The webservice also support a admin portal where the rules can be created/edited by business users. Application make use of an inmemory database to store the rules and further save it in an inmemory cache for better throughput and minimize I/O.Security of the APIs are treated differently for eligibilty and rules endpoints , first one is secured by API key based auth and the later by a JWT based auth methodolgy. 

## Technology stack
      - Spring Boot 2.5.1.RELEASE (Modules - web,test,security,actuator,jpa)
      - Spring 5.3.8.RELEASE
      - HikariCP 4.0.3
      - H2 in-memory database 1.4.200
      - Hibernate JPA
      - JsonWebToken 0.9.1
      - Logback
      - Maven 3
      - Java 8
      
## Prerequisites
     - JDK 1.8 or above
     - Maven 3.5 or above
     - git

## How to run in your local 
  1. clone the repository to your working directory.
  2. build the application , run  ```mvn clean install```
  3. step 2 will build the application and successful build will create a jar file under _target_ folder
  4. start the springboot application , run ```java -jar -Dspring.profiles.active=dev target/item-eligility-checker-0.0.1-SNAPSHOT.jar ``` 
  5. A clean startup will end with a log message _Started ItemEligilityCheckerApplication_
     
## REST Endpoints

### Functional APIs

| HTTP Verb        | URL           | Description  | Status Codes |
| ------------- |-------------|-----| ----|
| `POST` | `http://localhost:8080/v1/shipping/item/eligible` | Check the item is eligible for new shipping program.An API key need to be supplied by the consumer.| <ul><li>`200 OK`</li><l1> response will be true or false</li></ul>|
| `POST` | `http://localhost:8080/authenticate` | This endpoint is used to authenticate a business user and establishes a JWT for the successful auth| <ul><li>`200 OK` Authorized and JWT generated</li><li>`401 Unauthorized` if unauthorized</li></ul> |
| `POST` | `http://localhost:8080/v1/rules/` | Creates a new shipping rule | <ul><li>`201 Created` if rule successfully created</li></ul> |
| `PUT` | `http://localhost:8080/v1/rules/{id}` | Updated an existing rule with the data contained in the request body | <ul><li>`200 OK` if rule succesfully updated</li><li>`404 Not Found` if rule does not exist</li></ul> |
| `DELETE` | `http://localhost:8080/rules/{id}` | Deletes an existing rule that corresponds to the supplied rule ID | <ul><li>`204 No Content` if rule succesfully deleted</li><li>`404 Not Found` if rule does not exist</li></ul> |

### Monitoring APIs

| HTTP Verb        | URL           | Description  | Status Codes |
| ------------- |-------------|-----| ----|
| `GET` | `http://localhost:8081/manage/health` |Shows application health information| <ul><li>`200 OK` if the system is avaialable and healthy</li> <l1>`503 Internal Server Error` if the system is in error state</li></ul>|
| `GET` | `http://localhost:8081/manage/env` | Exposes properties from Spring’s ConfigurableEnvironment.| <ul><li>`200 OK` </li></ul> |
| `GET` | `http://localhost:8081/manage/metrics` | Shows ‘metrics’ information for the current application.| <ul><li>`200 OK`</li></ul> |

