package org.vorobel.moneytransfer;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class TransferController implements CrudHandler {
    @Override
    public void getAll(@NotNull Context context) {

    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void create(@NotNull Context context) {
        Transfer transfer = context.bodyAsClass(Transfer.class);
        transfer.persistAndFlush();
        context.header("Location", "/transfers/" + transfer.getId());
        context.json(transfer);
        context.status(201);
    }

    @Override
    public void update(@NotNull Context context, @NotNull String s) {

    }

    @Override
    public void delete(@NotNull Context context, @NotNull String s) {

    }
}
