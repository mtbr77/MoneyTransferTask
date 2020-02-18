package org.vorobel.moneytransfer.service;

import io.javalin.core.JavalinConfig;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.util.function.Consumer;

public class SwaggerService {
    public static Consumer<JavalinConfig> getSwaggerPluginConfigConsumer() {
        return config -> config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
    }

    private static OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info().version("1.0").description("Money Transfer Task");
        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Swagger Documentation"));
    }

}

