# MoneyTransferTask

curl -i -X POST -d '{"balance":111111111111.01}' http://localhost:7000/accounts
curl -i -X GET http://localhost:7000/accounts

mvnw test

mvnw compile quarkus:dev

mvnw clean package
java -jar target/MoneyTransferTask-1.0-SNAPSHOT-runner.jar