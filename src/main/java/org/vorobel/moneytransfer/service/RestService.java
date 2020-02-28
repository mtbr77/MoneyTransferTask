package org.vorobel.moneytransfer.service;

import io.javalin.Javalin;
import org.vorobel.moneytransfer.controller.TransferController;
import org.vorobel.moneytransfer.controller.AccountController;
import static io.javalin.apibuilder.ApiBuilder.*;

public class RestService {
    private Javalin provider;
    private AccountController accountController = new AccountController();
    private TransferController transferController = new TransferController();

    public RestService() {
        System.out.println("create REST");
        if (ConfigurationService.isSwaggerNeeded()) {
            provider = Javalin.create(SwaggerService.getSwaggerPluginConfigConsumer());
        } else {
            provider = Javalin.create();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            provider.stop();
        }));
    }

    public static void main(String[] args) {
        new RestService().start();
    }

    public void start() {
        provider.start(ConfigurationService.getRestServicePort());

        System.out.println("PORT: " + provider.port());

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
