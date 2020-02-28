package org.vorobel.moneytransfer.controller;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import org.jetbrains.annotations.NotNull;
import org.vorobel.moneytransfer.model.Account;
import org.vorobel.moneytransfer.model.AccountsForLocking;
import org.vorobel.moneytransfer.model.Transfer;
import javax.persistence.LockModeType;

public class TransferController implements CrudHandler {

    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class, isArray = true))
    )
    @Override
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
        Transfer transfer = Transfer.findById(Long.valueOf(id));
        if (transfer != null) {
            ctx.json(transfer);
            ctx.status(200);
        } else ctx.status(404);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transfer.class))
    )
    @Override
    public void create(@NotNull Context ctx) {
        Transfer transfer = ctx.bodyValidator(Transfer.class)
                .check(Transfer::isValid)
                .get();

        AccountsForLocking accountsForLocking =  new AccountsForLocking(transfer.sourceId, transfer.destinationId);

        accountsForLocking.save();

        do {
            AccountsForLocking firstRegistered = AccountsForLocking.getFirstRegisteredAccountsForLockingWithTheSameIds(transfer);
            if(accountsForLocking.id == firstRegistered.id) {
                startTransfer(ctx, transfer);
                delete(firstRegistered);
                break;
            }
        } while(true);

        ctx.header("Location", "/transfers/" + transfer.id);
        ctx.json(transfer);
    }

    public void delete(AccountsForLocking registeredTransfer) {
        AccountsForLocking persistedEntity = AccountsForLocking.findById(registeredTransfer.id);
        persistedEntity.delete();
    }

    public void startTransfer(@NotNull Context ctx, Transfer transfer) {
        Account source = Account.findById(transfer.sourceId, LockModeType.PESSIMISTIC_WRITE);
        if (source != null && source.enoughForWithdraw(transfer.amount)) {
            Account destination = Account.findById(transfer.destinationId, LockModeType.PESSIMISTIC_WRITE);
            if (destination != null) {
                Account.update(source.withdraw(transfer.amount));
                Account.update(destination.deposit(transfer.amount));
                transfer.success = true;
                ctx.status(200);
            } else ctx.status(400);
        } else ctx.status(400);

        transfer.save();
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
    public void delete(@NotNull Context ctx, @NotNull String id) {
        ctx.status(405);
    }

    @OpenApi(
            responses = @OpenApiResponse(status = "200")
    )
    public void deleteAll(@NotNull Context ctx){
        Transfer.deleteAll();
        ctx.status(200);
    };
}
