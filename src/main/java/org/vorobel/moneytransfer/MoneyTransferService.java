package org.vorobel.moneytransfer;
import io.micronaut.context.ApplicationContext;
import lombok.Getter;
import lombok.Setter;

public class MoneyTransferService {
    @Setter
    @Getter
    private int restPort = ConfigurationService.getRestServicePort();

    private RESTService restService = RESTService.create();

    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.run();
        var moneyTransferService = new MoneyTransferService();
        moneyTransferService.run();
    }

    public void run() {
        restService.start(restPort);
    }

    public void stop() {
        restService.stop();
    }

}
