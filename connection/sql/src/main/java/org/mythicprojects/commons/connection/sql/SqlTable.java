package org.mythicprojects.commons.connection.sql;

import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.mythicprojects.commons.connection.sql.util.SqlFunction;
import org.mythicprojects.commons.util.Validate;

public class SqlTable {

    private final SqlDatabase database;
    private final String name;

    SqlTable(@NotNull SqlDatabase database, @NotNull String name) {
        Validate.notNull(database, "database cannot be null");
        Validate.notEmpty(name, "name cannot be null or empty");
        this.database = database;
        this.name = name;
    }

    public <T> @UnknownNullability T execute(@NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query) {
        return this.database.executeStatement(function, query, this.name);
    }

    public <T> CompletableFuture<@UnknownNullability T> executeAsync(@NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query) {
        return this.database.executeStatementAsync(function, query, this.name);
    }

    public <T> @UnknownNullability T execute(@NotNull SqlExecutable<T> executable) {
        return executable.execute(this.database, this.name);
    }

    public <T> CompletableFuture<@UnknownNullability T> executeAsync(@NotNull SqlExecutable<T> executable) {
        return executable.executeAsync(this.database, this.name);
    }

    public @NotNull String getName() {
        return this.name;
    }

}
