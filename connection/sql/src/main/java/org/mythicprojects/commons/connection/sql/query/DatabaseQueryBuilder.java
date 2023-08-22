package org.mythicprojects.commons.connection.sql.query;

import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.sql.SqlDatabase;

public class DatabaseQueryBuilder extends AbstractQueryBuilder<DatabaseQueryBuilder> {

    private final SqlDatabase database;
    private final String tableName;

    private DatabaseQueryBuilder(@NotNull SqlDatabase database, @NotNull String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    public void execute() {
        this.database.connect(connection -> this.execute(connection, this.tableName));
    }

    public void executeAsync() {
        this.database.connectAsync(connection -> this.execute(connection, this.tableName));
    }

    public static DatabaseQueryBuilder create(@NotNull SqlDatabase database, @NotNull String tableName) {
        return new DatabaseQueryBuilder(database, tableName);
    }

}
