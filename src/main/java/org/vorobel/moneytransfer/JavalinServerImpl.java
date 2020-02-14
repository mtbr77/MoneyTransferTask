package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import javax.inject.Singleton;
import static io.javalin.apibuilder.ApiBuilder.crud;

@Singleton
public class JavalinServerImpl implements HttpRESTServer {
    private Javalin server;

    @Override
    public void start(int port) {

        server = create().start(port);

        server.routes(() -> {
            crud("accounts/:id", new AccountController());
        });

        server.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result(e.getMessage());
        });
    }

    private Javalin create() {
        if (SwaggerService.isNeeded()) {
            return Javalin.create(SwaggerService.getSwaggerPluginConfigConsumer());
        }

        return Javalin.create();
    }

    @Override
    public void stop() {
        server.stop();
    }
}
