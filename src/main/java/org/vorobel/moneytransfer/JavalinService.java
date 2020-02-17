package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import io.micronaut.context.ApplicationContext;

import static io.javalin.apibuilder.ApiBuilder.crud;

public class JavalinService extends RESTService {
    private Javalin provider;

    @Override
    public void start(int port) {
        provider.start(port);

        ApplicationContext appCtx = ApplicationContext.run();

        provider.routes(() -> {
            crud("accounts/:id", appCtx.getBean(AccountController.class));
            crud("transfers/:id", appCtx.getBean(TransferController.class));
        });

        provider.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result(e.getMessage());
        });
    }

    public JavalinService() {
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
    public void stop() {
        provider.stop();
    }
}
