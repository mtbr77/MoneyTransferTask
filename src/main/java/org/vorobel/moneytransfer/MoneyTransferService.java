package org.vorobel.moneytransfer;

import lombok.Getter;

public class MoneyTransferService {
    @Getter
    private int restPort;
    private RESTService restService;
    //private WeldContainer container;

    public MoneyTransferService() {
        restPort = ConfigurationService.getRestServicePort();
        init();
    }

    public MoneyTransferService(int restPort) {
        this.restPort = restPort;
        init();
    }

    private void init() {
        // weld = new Weld();
        //container = weld.initialize();
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
        //container.shutdown();
    }
}
