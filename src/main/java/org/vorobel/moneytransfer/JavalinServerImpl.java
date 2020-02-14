package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import javax.inject.Singleton;
import java.util.function.Consumer;

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
        if (ConfigurationService.isSwaggerNeeded()) {
            return Javalin.create(getSwaggerPluginConfigConsumer());
        }

        return Javalin.create();
    }

    @Override
    public void stop() {

    }

    private Consumer<JavalinConfig> getSwaggerPluginConfigConsumer() {
        return config -> config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
    }


    private OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info().version("1.0").description("Money Transfer Task");
        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Swagger Documentation"));
    }
}
