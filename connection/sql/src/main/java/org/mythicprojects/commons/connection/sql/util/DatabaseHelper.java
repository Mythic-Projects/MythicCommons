package org.mythicprojects.commons.connection.sql.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static @Nullable Long getLong(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        long value = resultSet.getLong(columnName);
        return resultSet.wasNull() ? null : value;
    }

    public static @Nullable Integer getInt(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);
        return resultSet.wasNull() ? null : value;
    }

    public static @Nullable Short getShort(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        short value = resultSet.getShort(columnName);
        return resultSet.wasNull() ? null : value;
    }

    public static @Nullable Byte getByte(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        byte value = resultSet.getByte(columnName);
        return resultSet.wasNull() ? null : value;
    }

    public static @Nullable Double getDouble(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        double value = resultSet.getDouble(columnName);
        return resultSet.wasNull() ? null : value;
    }

    public static @Nullable Float getFloat(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        float value = resultSet.getFloat(columnName);
        return resultSet.wasNull() ? null : value;
    }

    public static @Nullable Instant getInstant(@NotNull ResultSet resultSet, @NotNull String columnName) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnName);
        return timestamp == null ? null : timestamp.toInstant();
    }

    public static <V> @NotNull List<V> processResultSetToList(@NotNull ResultSet resultSet, @NotNull SqlFunction<ResultSet, V> resultProcessor) throws SQLException {
        List<V> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(resultProcessor.apply(resultSet));
        }
        return results;
    }

    public static <V> @NotNull Set<V> processResultSetToSet(@NotNull ResultSet resultSet, @NotNull SqlFunction<ResultSet, V> resultProcessor) throws SQLException {
        Set<V> results = new LinkedHashSet<>();
        while (resultSet.next()) {
            results.add(resultProcessor.apply(resultSet));
        }
        return results;
    }

    public static <K, V> @NotNull Map<K, V> processResultSetToMap(@NotNull ResultSet resultSet, @NotNull SqlFunction<ResultSet, K> keyProcessor, @NotNull SqlFunction<ResultSet, V> valueProcessor) throws SQLException {
        Map<K, V> results = new LinkedHashMap<>();
        while (resultSet.next()) {
            results.put(keyProcessor.apply(resultSet), valueProcessor.apply(resultSet));
        }
        return results;
    }

}
