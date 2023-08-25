package org.mythicprojects.commons.connection.sql.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class DatabaseHelper {

    private DatabaseHelper() {
    }

    public static @NotNull String replaceTable(@NotNull String tablePlaceholder, @NotNull String statement, @NotNull String tableName) {
        while (statement.contains(tablePlaceholder)) {
            statement = statement.replace(tablePlaceholder, tableName);
        }
        return statement;
    }

    public static @NotNull String replaceTable(@NotNull String statement, @NotNull String tableName) {
        return replaceTable("{TABLE}", statement, tableName);
    }

    public static void completeStatement(@NotNull PreparedStatement statement, @NotNull List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + 1, params.get(i));
        }
    }

    public static void completeStatement(@NotNull PreparedStatement statement, @NotNull Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

}
