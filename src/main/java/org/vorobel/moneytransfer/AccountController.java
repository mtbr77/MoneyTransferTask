package org.vorobel.moneytransfer;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.json.JavalinJson;
import io.javalin.plugin.openapi.annotations.*;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class AccountController implements CrudHandler {
    @Inject
    private AccountRepository accountRepository;

    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class, isArray = true))
    )
    @Override
    public void getAll(@NotNull Context ctx) {
        ctx.status(200);
        ctx.json(accountRepository.findAll());
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void getOne(@NotNull Context ctx, @NotNull String id) {
        ctx.status(200);
        ctx.json(accountRepository.findById(Long.valueOf(id)).orElse(null));
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "201", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void create(@NotNull Context ctx) {
        //Gson gson = new GsonBuilder().create();
        //JavalinJson.setFromJsonMapper(gson::fromJson);
        //JavalinJackson.getObjectMapper()
        Account account = ctx.bodyAsClass(Account.class);
        accountRepository.save(account);
        ctx.status(201);
        ctx.header("Location", "/accounts/" + account.getId());
        ctx.json(account);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void update(@NotNull Context ctx, @NotNull String s) {

    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void delete(@NotNull Context ctx, @NotNull String s) {

    }
}
