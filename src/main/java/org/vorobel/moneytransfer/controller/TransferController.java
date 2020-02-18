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
import java.sql.Timestamp;

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
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    @Transactional
    public void create(@NotNull Context context) {
        Transfer transfer = context.bodyAsClass(Transfer.class);
        Account sourceAccount = Account.findById(transfer.source, LockModeType.PESSIMISTIC_WRITE);

        if (sourceAccount != null && sourceAccount.enoughForWithdraw(transfer.amount)) {
            Account destinationAccount = Account.findById(transfer.destination, LockModeType.PESSIMISTIC_WRITE);
            if (destinationAccount != null) {
                sourceAccount.update("balance", sourceAccount.withdraw(transfer.amount));
                destinationAccount.update("balance", destinationAccount.deposit(transfer.amount));
                transfer.success = true;
                context.status(200);
            } else context.status(400);
        } else context.status(400);

        transfer.time = new Timestamp(System.currentTimeMillis());
        transfer.persist();
        context.header("Location", "/transfers/" + transfer.id);
        context.json(transfer);
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
