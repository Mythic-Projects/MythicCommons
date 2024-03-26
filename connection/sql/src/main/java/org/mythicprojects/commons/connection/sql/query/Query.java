package org.mythicprojects.commons.connection.sql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.mythicprojects.commons.connection.sql.SqlExecutable;
import org.mythicprojects.commons.connection.sql.util.DatabaseHelper;
import org.mythicprojects.commons.connection.sql.util.SqlFunction;
import org.mythicprojects.commons.util.Validate;

public class Query<T> implements SqlExecutable<T> {

    private final String query;
    private final List<Object> parameters;

    private SqlFunction<ResultSet, T> resultProcessor;

    public Query(@NotNull String query, @NotNull List<Object> parameters) {
        this.query = Validate.notEmpty(query, "query cannot be null or empty");
        this.parameters = Validate.notNull(parameters, "parameters cannot be null");
    }

    public Query(@NotNull String query, @NotNull Object... parameters) {
        this(query, Arrays.asList(parameters));
    }

    public Query(@NotNull String query) {
        this(query, Collections.emptyList());
    }

    @Contract("_ -> this")
    public Query<T> resultProcessor(@NotNull SqlFunction<ResultSet, T> resultProcessor) {
        Validate.notNull(resultProcessor, "resultProcessor cannot be null");
        this.resultProcessor = resultProcessor;
        return this;
    }

    @Override
    public @UnknownNullability T execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DatabaseHelper.replaceTable(this.query, tableName))) {
            DatabaseHelper.completeStatement(statement, this.parameters);
            if (this.resultProcessor == null) {
                statement.executeUpdate();
                return null;
            }
            ResultSet result = statement.executeQuery();
            return this.resultProcessor.apply(result);
        }
    }

}
