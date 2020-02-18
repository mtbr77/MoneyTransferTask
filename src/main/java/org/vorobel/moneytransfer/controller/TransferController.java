package org.vorobel.moneytransfer.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import org.jetbrains.annotations.NotNull;
import org.vorobel.moneytransfer.model.Account;
import org.vorobel.moneytransfer.model.Transfer;

import javax.inject.Singleton;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@Singleton
public class TransferController implements CrudHandler {
    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class, isArray = true))
    )
    @Override
    public void getAll(@NotNull Context context) {
        context.status(200);
        context.json(Transfer.listAll());
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        Transfer transfer = Transfer.findById(id);
        if (transfer != null) {
            context.json(transfer);
            context.status(200);
        } else context.status(404);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "201", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    @Transactional
    public void create(@NotNull Context context) {
        Transfer transfer = context.bodyAsClass(Transfer.class);
        Account account = Account.findById(transfer.sourceId, LockModeType.PESSIMISTIC_WRITE);
        if (account != null) {
            Account newAccount = context.bodyAsClass(Account.class);
            account.update("balance", newAccount.balance);
            context.json(account);
            context.status(200);
        } else context.status(404);

        transfer.persistAndFlush();
        context.header("Location", "/transfers/" + transfer.id);
        context.json(transfer);
        context.status(201);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "405", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    public void update(@NotNull Context context, @NotNull String id) {
        context.status(405);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "405", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    @Transactional
    public void delete(@NotNull Context context, @NotNull String id) {
        context.status(405);
    }
}
