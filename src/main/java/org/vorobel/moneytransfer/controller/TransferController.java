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
    @ActivateRequestContext
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
    @ActivateRequestContext
    public void create(@NotNull Context ctx) {
        Transfer transfer = ctx.bodyValidator(Transfer.class)
                .check(Transfer::isValid)
                .get();

        AccountsForLocking accountsForLocking =  new AccountsForLocking(transfer.sourceId, transfer.destinationId);

        registerAccountsForLocking(accountsForLocking);

        String accountsAlreadyInvolved = "sourceId = ?1 or destinationId = ?1 or sourceId = ?2 or destinationId = ?2";

        do {
            AccountsForLocking firstRegistered = getFirstRegisteredAccountsForLockingWithTheSameIds(transfer, accountsAlreadyInvolved);
            if(accountsForLocking.id == firstRegistered.id) {
                startTransfer(ctx, transfer);
                delete(firstRegistered);
                break;
            }
        } while(true);

        ctx.header("Location", "/transfers/" + transfer.id);
        ctx.json(transfer);
    }

    @ActivateRequestContext
    @Transactional
    public AccountsForLocking getFirstRegisteredAccountsForLockingWithTheSameIds(Transfer transfer, String accountsAlreadyInvolved) {
        return AccountsForLocking.find(accountsAlreadyInvolved, transfer.sourceId, transfer.destinationId).firstResult();
    }

    @ActivateRequestContext
    @Transactional
    public void delete(AccountsForLocking registeredTransfer) {
        AccountsForLocking persistedEntity = AccountsForLocking.findById(registeredTransfer.id);
        persistedEntity.delete();
        persistedEntity.flush();
    }

    @ActivateRequestContext
    @Transactional
    public void registerAccountsForLocking(AccountsForLocking accountsForLocking) {
        accountsForLocking.persistAndFlush();
    }

    @ActivateRequestContext
    @Transactional
    public void startTransfer(@NotNull Context ctx, Transfer transfer) {
        Account source = Account.findById(transfer.sourceId, LockModeType.PESSIMISTIC_WRITE);
        if (source != null && source.enoughForWithdraw(transfer.amount)) {
            Account destination = Account.findById(transfer.destinationId, LockModeType.PESSIMISTIC_WRITE);
            if (destination != null) {
                Account.update("set balance = ?1 where id = ?2", source.withdraw(transfer.amount), source.id);
                Account.update("set balance = ?1 where id = ?2", destination.deposit(transfer.amount), destination.id);
                transfer.success = true;
                ctx.status(200);
            } else ctx.status(400);
        } else ctx.status(400);

        transfer.persistAndFlush();
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
    @Transactional
    @ActivateRequestContext
    public void deleteAll(@NotNull Context ctx){
        Transfer.deleteAll();
        ctx.status(200);
    };
}
