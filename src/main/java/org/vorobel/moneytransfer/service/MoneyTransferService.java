package org.vorobel.moneytransfer.service;

import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoneyTransferService {
    @Getter
    @Setter
    private int restPort;

    @Inject
    RESTService restService;

    public void onStart(@Observes StartupEvent event) {
        restService.start(restPort);
    }

    public MoneyTransferService() {
        restPort = ConfigurationService.getRestServicePort();
    }

    public void stop() { restService.stop(); }
}
