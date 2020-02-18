package org.vorobel.moneytransfer.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.vorobel.moneytransfer.model.Account;

import javax.inject.Singleton;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@Singleton
public class AccountController implements CrudHandler {
    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class, isArray = true))
    )
    @Override
    public void getAll(@NotNull Context context) {
        context.status(200);
        context.json(Account.listAll());
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        Account account = Account.findById(id);
        if (account != null) {
            context.json(account);
            context.status(200);
        } else context.status(404);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "201", content = @OpenApiContent(from = Account.class))
    )
    @Override
    @Transactional
    public void create(@NotNull Context context) {
        Account account = context.bodyAsClass(Account.class);
        account.persistAndFlush();
        context.header("Location", "/accounts/" + account.id);
        context.json(account);
        context.status(201);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void update(@NotNull Context context, @NotNull String id) {
        Account account = Account.findById(id, LockModeType.PESSIMISTIC_WRITE);
        if (account != null) {
            Account newAccount = context.bodyAsClass(Account.class);
            account.update("balance", newAccount.balance);
            context.json(account);
            context.status(200);
        } else context.status(404);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    @Transactional
    public void delete(@NotNull Context context, @NotNull String id) {
        Account account = Account.findById(id, LockModeType.PESSIMISTIC_WRITE);
        if (account != null) {
            account.delete();
            context.status(200);
        } else context.status(404);
    }
}
