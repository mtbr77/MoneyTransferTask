package org.vorobel.moneytransfer.service;

import io.javalin.Javalin;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.StartupEvent;
import org.vorobel.moneytransfer.controller.TransferController;
import org.vorobel.moneytransfer.controller.AccountController;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.javalin.apibuilder.ApiBuilder.*;

//@ApplicationScoped
public class RestService {
    private Javalin provider;
    @Inject
    AccountController accountController;
    @Inject
    TransferController transferController;

    public void initBeanFromQuarkusContext(@Observes StartupEvent event) {
        start();
    }

    public RestService() {
        if (ConfigurationService.isSwaggerNeeded()) {
            provider = Javalin.create(SwaggerService.getSwaggerPluginConfigConsumer());
        } else {
            provider = Javalin.create();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            provider.stop();
        }));
    }

    public void start() {
        provider.start(ConfigurationService.getRestServicePort());

        provider.routes(() -> {
            crud("/accounts/:id", accountController);
            crud("/transfers/:id", transferController);
        });

        provider.delete("/accounts", (ctx) -> {
            accountController.deleteAll(ctx);
        });

        provider.delete("/transfers", (ctx) -> {
            transferController.deleteAll(ctx);
        });

        provider.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(400);
            ctx.result(e.getMessage());
        });
    }

    public void stop() { provider.stop(); }
}
