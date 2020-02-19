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

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Singleton;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@Singleton
public class TransferController implements CrudHandler {
    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class, isArray = true))
    )
    @Override
    @ActivateRequestContext
    public void getAll(@NotNull Context ctx) {
        ctx.status(200);
        ctx.json(Transfer.listAll());
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    public void getOne(@NotNull Context ctx, @NotNull String id) {
        Transfer transfer = Transfer.findById(id);
        if (transfer != null) {
            ctx.json(transfer);
            ctx.status(200);
        } else ctx.status(404);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    @Transactional
    public void create(@NotNull Context ctx) {
        Transfer transfer = ctx.bodyValidator(Transfer.class)
                .check(obj -> obj.isValidAmount())
                .get();
        Account sourceAccount = Account.findById(transfer.source, LockModeType.PESSIMISTIC_WRITE);

        if (sourceAccount != null && sourceAccount.enoughForWithdraw(transfer.amount)) {
            Account destinationAccount = Account.findById(transfer.destination, LockModeType.PESSIMISTIC_WRITE);
            if (destinationAccount != null) {
                sourceAccount.update("balance", sourceAccount.withdraw(transfer.amount));
                destinationAccount.update("balance", destinationAccount.deposit(transfer.amount));
                transfer.success = true;
                ctx.status(200);
            } else ctx.status(400);
        } else ctx.status(400);

        transfer.persistAndFlush();
        ctx.header("Location", "/transfers/" + transfer.id);
        ctx.json(transfer);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "405", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    public void update(@NotNull Context ctx, @NotNull String id) {
        ctx.status(405);
    }

    @OpenApi(
            pathParams = @OpenApiParam(name = "id"),
            responses = @OpenApiResponse(status = "405", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    @Transactional
    public void delete(@NotNull Context ctx, @NotNull String id) {
        ctx.status(405);
    }
}
