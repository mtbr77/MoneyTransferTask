package org.vorobel.moneytransfer.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.vorobel.moneytransfer.model.Account;

import javax.persistence.LockModeType;

public class AccountController implements CrudHandler {
    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class, isArray = true))
    )
    @Override
    public void getAll(@NotNull Context ctx) {
        ctx.status(200);
        ctx.json(Account.listAll());
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void getOne(@NotNull Context ctx, @NotNull String id) {
        Account account = Account.findById(Long.valueOf(id));
        if (account != null) {
            ctx.json(account);
            ctx.status(200);
        } else ctx.status(404);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "201", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void create(@NotNull Context ctx) {
        Account account = ctx.bodyValidator(Account.class)
                .check(obj -> obj.isValidBalance())
                .get();
        account.save();
        ctx.header("Location", "/accounts/" + account.id);
        ctx.json(account);
        ctx.status(201);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void update(@NotNull Context ctx, @NotNull String id) {
        Account account = Account.findById(Long.valueOf(id), LockModeType.PESSIMISTIC_WRITE);
        if (account != null) {
            Account newAccount = ctx.bodyAsClass(Account.class);
            account.balance = newAccount.balance;
            Account.update(account);
            ctx.json(account);
            ctx.status(200);
        } else ctx.status(404);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
    )
    @Override
    public void delete(@NotNull Context ctx, @NotNull String id) {
        Account account = Account.findById(Long.valueOf(id), LockModeType.PESSIMISTIC_WRITE);
        if (account != null) {
            account.delete();
            ctx.status(200);
        } else ctx.status(404);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "200")
    )
    public void deleteAll(@NotNull Context ctx) {
        Account.deleteAll();
        ctx.status(200);
    };
}
