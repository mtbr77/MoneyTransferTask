# MoneyTransferTask

### Technologies:
- Javalin framework for REST
- H2 in memory database
- Quarkus framework for persistence

### Solution for concurrent requests from multiple systems and services on behalf of end users:
PESSIMISTIC locking for accounts and additional table that saves accounts to be locked   

### REST service & Swagger-UI:
http://localhost:7000
http://localhost:7000/swagger-ui

### Examples of REST requests:
curl -i -X POST -d '{"balance" : 111.01}' http://localhost:7000/accounts
curl -i -X GET http://localhost:7000/accounts
curl -i -X GET http://localhost:7000/accounts/1
curl -i -X PUT -d '{"balance" : 0}' http://localhost:7000/accounts/1
curl -i -X DELETE http://localhost:7000/accounts/1
curl -i -X DELETE http://localhost:7000/accounts

curl -i -X POST -d '{"sourceId" : 1 , "destinationId" : 2 , "amount" : 11.01}' http://localhost:7000/transfers
curl -i -X GET http://localhost:7000/transfers
curl -i -X GET http://localhost:7000/transfers/3

### How to run:
Run tests: mvnw test
Run application: mvnw compile quarkus:dev
or
mvnw clean package
java -jar target/MoneyTransferTask-1.0-SNAPSHOT-runner.jar

### Configuration
see application.properties

For load tests the next numbers is used:
tests.load.accountsNumber = 20
tests.load.transfersNumber = 200
tests.load.initialBalance = 100
tests.load.transferAmount = 1.01

