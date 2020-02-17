package org.vorobel.moneytransfer;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

public class AccountController implements CrudHandler {
    AccountRepository accountRepository = new AccountRepository();

    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class, isArray = true))
    )
    @Override
    public void getAll(@NotNull Context context) {
        context.status(200);
        context.json(accountRepository.findAll());
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        //404
        //context.json(accountRepository.findById(Long.valueOf(id)).orElse(null));
        context.status(200);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "201", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void create(@NotNull Context context) {
        Account account = context.bodyAsClass(Account.class);
        accountRepository.persist(account);
        context.header("Location", "/accounts/" + account.getId());
        context.json(account);
        context.status(201);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void update(@NotNull Context context, @NotNull String idString) {
        long id = Long.valueOf(idString);
        Account account = accountRepository.findById(id);
        if (account != null) {
            Account newAccount = context.bodyAsClass(Account.class);
            newAccount.setId(id);
            accountRepository.persist(newAccount);
            context.json(newAccount);
            context.status(200);
        } else context.status(404);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void delete(@NotNull Context context, @NotNull String idString) {
        long id = Long.valueOf(idString);
        Account account = accountRepository.findById(id);
        accountRepository.delete(account);
        context.status(200);
    }
}
