package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.crud;

public class JavalinService implements RESTService {
    private Javalin provider;

    @Override
    public void start(int port) {
        provider.start(port);

        provider.routes(() -> {
            crud("accounts/:id", new AccountController());
        });

        provider.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result(e.getMessage());
        });
    }

    public JavalinService() {
        if (SwaggerService.isNeeded()) {
            provider = Javalin.create(SwaggerService.getSwaggerPluginConfigConsumer());
        } else {
            provider = Javalin.create();
        }
    }

    @Override
    public void stop() {
        provider.stop();
    }
}
