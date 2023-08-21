package org.mythicprojects.commons.connection.sql;

import java.sql.Connection;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SqlExecutable {

    void execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException;

    default void execute(@NotNull SqlDatabase database, @NotNull String tableName) {
        database.connect(connection -> this.execute(connection, tableName));
    }

    default void execute(@NotNull SqlTable table) {
        table.execute(this);
    }

}
