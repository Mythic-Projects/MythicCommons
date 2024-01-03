package org.mythicprojects.commons.connection.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface SqlExecutable<T> {

    @UnknownNullability T execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException;

    @Blocking
    default @UnknownNullability T execute(@NotNull SqlDatabase database, @NotNull String tableName) {
        return database.connect(connection -> this.execute(connection, tableName));
    }

    @NonBlocking
    default CompletableFuture<@UnknownNullability T> executeAsync(@NotNull SqlDatabase database, @NotNull String tableName) {
        return database.connectAsync(connection -> this.execute(connection, tableName));
    }

    @Blocking
    default @UnknownNullability T execute(@NotNull SqlTable table) {
        return table.execute(this);
    }

    @NonBlocking
    default CompletableFuture<@UnknownNullability T> executeAsync(@NotNull SqlTable table) {
        return table.executeAsync(this);
    }

}
