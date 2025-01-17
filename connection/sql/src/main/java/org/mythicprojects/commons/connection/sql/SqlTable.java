package org.mythicprojects.commons.connection.sql;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.mythicprojects.commons.connection.sql.util.SqlFunction;
import org.mythicprojects.commons.util.Validate;

public class SqlTable {

    private final Reference<SqlDatabase> databaseRef;
    private final String name;

    SqlTable(@NotNull SqlDatabase database, @NotNull String name) {
        Validate.notNull(database, "database cannot be null");
        Validate.notEmpty(name, "name cannot be null or empty");
        this.databaseRef = new WeakReference<>(database);
        this.name = name;
    }

    public <T> @UnknownNullability T execute(@NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query) {
        return this.getDatabase().executeStatement(function, query, this.name);
    }

    public <T> CompletableFuture<@UnknownNullability T> executeAsync(@NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query) {
        return this.getDatabase().executeStatementAsync(function, query, this.name);
    }

    public <T> @UnknownNullability T execute(@NotNull SqlExecutable<T> executable) {
        return executable.execute(this.getDatabase(), this.name);
    }

    public <T> CompletableFuture<@UnknownNullability T> executeAsync(@NotNull SqlExecutable<T> executable) {
        return executable.executeAsync(this.getDatabase(), this.name);
    }

    public @NotNull String getName() {
        return this.name;
    }

    private @NotNull SqlDatabase getDatabase() {
        SqlDatabase database = Validate.notNull(this.databaseRef.get(), "Database has been garbage collected");
        Validate.isTrue(database.isOpen(), "Database is closed");
        return database;
    }

}
