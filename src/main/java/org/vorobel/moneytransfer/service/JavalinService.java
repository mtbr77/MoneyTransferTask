package org.vorobel.moneytransfer.service;

import io.javalin.Javalin;
import org.vorobel.moneytransfer.controller.TransferController;
import org.vorobel.moneytransfer.controller.AccountController;

import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.crud;

@Singleton
public class JavalinService implements RESTService {
    private Javalin provider;

    @Inject
    AccountController accountController;

    @Inject
    TransferController transferController;


    public JavalinService() {
        System.out.println("Javalin Started");
        if (ConfigurationService.isSwaggerNeeded()) {
            provider = Javalin.create(SwaggerService.getSwaggerPluginConfigConsumer());
        } else {
            provider = Javalin.create();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            provider.stop();
        }));
    }

    @Override
    public void start(int port) {
        provider.start(port);

        provider.routes(() -> {
            crud("accounts/:id", accountController);
            crud("transfers/:id", transferController);
        });

        provider.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result(e.getMessage());
        });
    }

    public void stop() { provider.stop(); }
}
