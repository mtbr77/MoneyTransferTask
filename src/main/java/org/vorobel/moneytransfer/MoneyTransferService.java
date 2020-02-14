package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.micronaut.context.ApplicationContext;
import io.swagger.v3.oas.models.info.Info;
import lombok.Getter;

import javax.inject.Inject;
import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MoneyTransferService {
    @Getter
    private int port;

    @Inject
    private HttpRESTServer httpRESTServer;

    public static void main(String[] args) {
        int port = ConfigurationService.getServicePort();
        var moneyTransferService = new MoneyTransferService(port);
        moneyTransferService.run();
    }

    public MoneyTransferService(int port) {
        this.port = port;
        ApplicationContext.run();
    }

    public void run() {
        httpRESTServer.start(port);
    }

    public void stop() {
        httpRESTServer.stop();
    }

}
