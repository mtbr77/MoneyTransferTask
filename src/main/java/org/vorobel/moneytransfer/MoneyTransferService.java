package org.vorobel.moneytransfer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;

public class MoneyTransferService {
    @Property(name = "httpserver.port")
    int port;

    private RESTService restService = new JavalinService();

    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.run();
        var moneyTransferService = new MoneyTransferService();
        moneyTransferService.run();
    }

    public void run() {
        restService.start(port);
    }

    public void stop() {
        restService.stop();
    }

}
