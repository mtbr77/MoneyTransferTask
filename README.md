# MoneyTransferTask

curl -i -X POST -d '{"balance" : 111111111111.01}' http://localhost:7000/accounts
curl -i -X GET http://localhost:7000/accounts
curl -i -X POST -d '{"source" : 1 , "destination" : 2 , "amount" : 11.01}' http://localhost:7000/transfers

Run tests: mvnw test

Run: mvnw compile quarkus:dev

mvnw clean package
java -jar target/MoneyTransferTask-1.0-SNAPSHOT-runner.jar