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


## Design



### High Level component design
![High level component design ](https://user-images.githubusercontent.com/9518659/121952139-b41e7d80-cd10-11eb-8ec0-eb24206081d3.jpg)


### Assumptions
1. Presence of distributed cloud computing components like Load balancers, API gateway and deployment orchestrator like kubernetes or openshift.
2. Eligibility APIs are exposed to internal microservices and not exposed over the internet.
3. Endpoint versioning done at the API gateway.
4. At any point, no of active rules will not exceed 100.
5. Packaged rules and eligibility APIs in a single container but it can be split if needed.
6. Eligibilty service expects a high TPS and API gateway can enfore a rate limiter to prevent any accidental overload of the compute instance.

### Storage Requirements

We can use an inmemory datastore like H2 and when we move to production we can use persistant stores like  MySQL or Redis.

Database - RuleDB

Tables 

1. RuleDefinition
      | Column        | Data type |Primary key           | Unique Key |
      | ------------- |-------------|-----| ----|
      | ID            |BIGINT NOT NULL|Yes|Yes|
      | key| VARCHAR(255)|No|Yes|
      | value|VARCHAR(255)|No|No|


### Deep dive

#### Item Eligibilty Service 

This is the key component of the app, service evaluates the payload based on the preconfigured rules. At present the preconfigured rules are 

1.shipping.program.approved.sellers - Define a list of approved sellers.
2.shipping.program.approved.categories - Define a list of approved categories.
3.shipping.program.approved.price - A minimum qualified price.

The rules will be first looked up in the cache and if not present will be going to database , an inmemory cache and an inmemory H2 database is used to achieve this design.In production we need to replace the database with persistant databases like MySQL,Mongo etc but the cache can still be JVM based as we are not expecting a huge set of rules created.
Since this service is consumed by other microservices, the authentication can be based on API-KEYs as it will make the process faster still securing access. A custom http filter is defined to intercept the request and perform the token validation and if valid, mark the request as authenticated, this in conjunction with a custom ```WebSecurityConfigurerAdapter``` will instruct spring security to inject the filter before the default ```UsernamePasswordAuthenticationFilter```.

API : v1/shipping/item/eligible

Usage :  
``` 
curl -X POST \
  http://localhost:8080/v1/shipping/item/eligible \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplZXYiLCJleHAiOjE2MjM2MjE2NjksImlhdCI6MTYyMzYwMzY2OX0.QaeSWTfDbgDjjOcXnIIm5do8CutKmBgNssM5xqwxWy80yJ06_EKaclkmQ3NejJC3_SXGdTM1fR6EYxF7o1dyGw' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: d6f79200-55c3-4b8e-951d-7b7b6a7f3265' \
  -H 'X-API-KEY: aec093c2-c981-44f9-9a4a-365ad1d2f05e' \
  -H 'cache-control: no-cache' \
  -d '{
"title" : "iphone",
"seller":"john",
"category":"12",
"price":"99.00"
}'

```

#### Rule Management Service 

A management portal for defining rules needs to be developed and our webservice exposes a set of APIs which can perform the basic CRUD operations.The API accessibility is limited  only to authenticated users, clients will be invoking an authenticate API which sends a JWT token by looking up the user database(here we use preconfigured users for prototyping). Subsequent operations needs to pass this token in the AUTH header and service will grant access only if JWT token validation is succesfull. As mentioned above the rules will be stored in an inmemory H2 database and further synced to the inmemory cache.Any update/delete operation will trigger an eviciton from the cache, the idea is to remove stale data and reload the cache organically when eligiblity service makes the next rule evaluation.

Database view 



API : /authenticate

Payload :
```
curl -X POST \
  http://localhost:8080/authenticate \
  -H 'Content-Type: application/json' \
  -d '{ 
"username":"rajeev",
"password":"changeit"
}'

```
Response : 

```
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplZXYiLCJleHAiOjE2MjM3MDI1MzUsImlhdCI6MTYyMzY4NDUzNX0.jwq0FLpI-yMs5xHUNjETOjwIwQRE8x-5yR6VRPSCABAbHWUabco0UihYsIbf9ndYO--bEtrLu571Ev-xffRobg",
    "ttl" : 120000
}
```

#### Create rule 
      
   API : POST /v1/rules
   Payload : 
```
curl -X POST \
  http://localhost:8080/v1/rules \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplZXYiLCJleHAiOjE2MjM2MjE2NjksImlhdCI6MTYyMzYwMzY2OX0.QaeSWTfDbgDjjOcXnIIm5do8CutKmBgNssM5xqwxWy80yJ06_EKaclkmQ3NejJC3_SXGdTM1fR6EYxF7o1dyGw' \
  -H 'Content-Type: application/json' \
  -d '{ 
"rule":"shipping.program.approved.sellers",
"value":"john,rajeev"
}'
```

#### Update rule

API : PUT /v1/rules/{{id}}

Payload : 
```
curl -X PUT \
  http://localhost:8080/v1/rules/1 \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplZXYiLCJleHAiOjE2MjM2MjE2NjksImlhdCI6MTYyMzYwMzY2OX0.QaeSWTfDbgDjjOcXnIIm5do8CutKmBgNssM5xqwxWy80yJ06_EKaclkmQ3NejJC3_SXGdTM1fR6EYxF7o1dyGw' \
  -H 'Content-Type: application/json' \
  -d '{
        "rule": "shipping.program.approved.sellers",
        "value": "rajeev,john"
    }'
    
```

#### Delete rule

API : DELETE /v1/rules/{{id}}

Payload : 
```
curl -X DELETE \
  http://localhost:8080/v1/rules/1 \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplZXYiLCJleHAiOjE2MjM2MjE2NjksImlhdCI6MTYyMzYwMzY2OX0.QaeSWTfDbgDjjOcXnIIm5do8CutKmBgNssM5xqwxWy80yJ06_EKaclkmQ3NejJC3_SXGdTM1fR6EYxF7o1dyGw' \
  -H 'Content-Type: application/json'

```

#### List rules

API : GET /v1/rules

Payload : 
```
curl -X GET \
  http://localhost:8080/v1/rules/ \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWplZXYiLCJleHAiOjE2MjM2MjE2NjksImlhdCI6MTYyMzYwMzY2OX0.QaeSWTfDbgDjjOcXnIIm5do8CutKmBgNssM5xqwxWy80yJ06_EKaclkmQ3NejJC3_SXGdTM1fR6EYxF7o1dyGw' \
  -H 'Content-Type: application/json'
 ```
 
#### Monitoring Service 

Spring boot actuator module provides an elegant way to monitor and manage your application when you push it to production.We make use of 3 key APIs exposed over HTTP to monitor the health of the application and gain insights on few key metrics like jvm memory usgae, cpu usgage, active jvm threads,http request,container threads,database connection pools etc.This information can be fed to  container orchestration tools like K8s to understand and react to the results like for eg if the if health check fails , K8s will not send any traffic to that instance.

1. API : GET manage/health

Payload :
```
curl -X GET \
  http://localhost:8081/manage/health
  
```
Response : 

```
{
    "status": "UP"
}
```
2. API : GET manage/metrics

Payload : 
```
curl -X GET \
  http://localhost:8081/manage/metrics
  
```


#### Unit testing

Added ```spring-boot-starter-test``` dependency to facilitate the unit test cases, key libraries used are ```MockMVC``` and ```Mockito```. 

#### Performance considerations

Application needs to be optimized for read-intensive workloads, and below considerations are incorporated
1. Uses Inmemory caching for frequently accessed data , rules in this scenario are frequently looked up and less frequently modified.
3. Uses HikariCP as a JDBC connection pool and correct setting of max pool ,idle pool and timeout will reduce ```getConnection()``` overheads.
4. Uses Logback Async appenders for logging without blocking the main thread.
5. Uses API key and JWT authenticaton mechanism which are light weight compared to OAUTH.
6. Nice to have , we know the business rules wont change frequently and once the rule is evaluated for an item, we can instruct gateway to cache the response(use HTTP cache headers).Any change in rule should trigger a cache purge.This approach can improve our API's performance through reduced latency and network traffic.



## References

1. https://docs.spring.io
2. https://github.com/brettwooldridge/HikariCP
3. 


