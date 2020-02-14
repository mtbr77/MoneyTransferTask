package org.vorobel.moneytransfer;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MoneyTransferService {
    public static void main(String[] args) {
        var moneyTransferService = new MoneyTransferService();
        moneyTransferService.run();
    }

    public MoneyTransferService() {

    }
    private void run() {
    }


}
