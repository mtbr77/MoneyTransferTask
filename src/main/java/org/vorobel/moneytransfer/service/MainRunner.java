package org.vorobel.moneytransfer.service;

import io.quarkus.runtime.StartupEvent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainRunner {
    @Inject
    RestService restService;

    public void onStart(@Observes StartupEvent event) {
        restService.start();
    }

    public void stop() { restService.stop(); }
}
