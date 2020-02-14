package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.crud;

public class HttpServerService {
    private static Javalin app;

    public static void start() {
        var swaggerPlugin = new OpenApiPlugin(getOpenApiOptions());
        Consumer<JavalinConfig> SwaggerPluginConfigConsumer = config -> config.registerPlugin(swaggerPlugin);
        Javalin app = Javalin.create(SwaggerPluginConfigConsumer).start(7000);

        app.routes(() -> {
            crud("accounts/:id", new AccountController());
        });

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.result(e.getMessage());
        });
    }

    private static OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("Money Transfer Task");
        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Swagger Documentation"));
    }

}
