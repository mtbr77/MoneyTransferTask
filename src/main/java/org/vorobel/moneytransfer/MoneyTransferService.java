package org.vorobel.moneytransfer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import lombok.Getter;
import lombok.Setter;
import javax.inject.Inject;

public class MoneyTransferService {
    @Property(name = "httpserver.port")
    int port;

    @Inject
    HttpRESTServer httpRESTServer;

    public static void main(String[] args) {
        var moneyTransferService = new MoneyTransferService();
        moneyTransferService.run();
    }

    public MoneyTransferService() {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            httpRESTServer = ctx.getBean(HttpRESTServer.class);
        }
    }

    public void run() {
        httpRESTServer.start(port);
    }

    public void stop() {
        httpRESTServer.stop();
    }

}
