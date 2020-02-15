package org.vorobel.moneytransfer;
import lombok.Getter;

public class MoneyTransferService {
    @Getter
    private int restPort;

    private RESTService restService;

    public MoneyTransferService() {
        restPort = ConfigurationService.getRestServicePort();
        init();
    }

    public MoneyTransferService(int restPort) {
        this.restPort = restPort;
        init();
    }

    private void init() {
        restService = RESTService.create();
    }

    public static void main(String[] args) {
        var service = new MoneyTransferService();
        service.start();
    }

    public void start() {
        restService.start(restPort);
    }

    public void stop() {
        restService.stop();
    }
}
