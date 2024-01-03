package org.mythicprojects.commons.connection.sql.query;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.mythicprojects.commons.connection.sql.SqlDatabase;

public class DatabaseQueryBuilder extends AbstractQueryBuilder<DatabaseQueryBuilder> {

    private final SqlDatabase database;
    private final String tableName;

    private DatabaseQueryBuilder(@NotNull SqlDatabase database, @NotNull String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    @Blocking
    public @Unmodifiable Map<Query<?>, Object> execute() {
        return this.execute(this.database, this.tableName);
    }

    @NonBlocking
    public @Unmodifiable CompletableFuture<Map<Query<?>, Object>> executeAsync() {
        return this.executeAsync(this.database, this.tableName);
    }

    @Contract("_, _ -> new")
    public static DatabaseQueryBuilder create(@NotNull SqlDatabase database, @NotNull String tableName) {
        return new DatabaseQueryBuilder(database, tableName);
    }

}
