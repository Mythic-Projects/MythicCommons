package org.mythicprojects.commons.connection.sql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.sql.SqlExecutable;
import org.mythicprojects.commons.connection.sql.util.DatabaseHelper;
import org.mythicprojects.commons.connection.sql.util.SqlConsumer;
import org.mythicprojects.commons.util.Validate;

public class Query implements SqlExecutable {

    private final String query;
    private final List<Object> parameters;

    private final Set<SqlConsumer<ResultSet>> resultConsumers = new LinkedHashSet<>();

    public Query(@NotNull String query, @NotNull List<Object> parameters) {
        Validate.notEmpty(query, "query cannot be null or empty");
        Validate.notNull(parameters, "parameters cannot be null");
        this.query = query;
        this.parameters = parameters;
    }

    public Query(@NotNull String query, @NotNull Object... parameters) {
        this(query, Arrays.asList(parameters));
    }

    public Query(@NotNull String query) {
        this(query, Collections.emptyList());
    }

    public @NotNull String getQuery() {
        return this.query;
    }

    public @NotNull List<Object> getParameters() {
        return this.parameters;
    }

    @Contract("_ -> this")
    public Query resultConsumer(@NotNull SqlConsumer<ResultSet> resultSetConsumer) {
        Validate.notNull(resultSetConsumer, "resultSetConsumer cannot be null");
        this.resultConsumers.add(resultSetConsumer);
        return this;
    }

    @Override
    public void execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DatabaseHelper.replaceTable(this.getQuery(), tableName))) {
            DatabaseHelper.completeStatement(statement, this.getParameters());
            if (this.resultConsumers.isEmpty()) {
                statement.executeUpdate();
                return;
            }

            ResultSet result = statement.executeQuery();
            for (SqlConsumer<ResultSet> resultConsumer : this.resultConsumers) {
                resultConsumer.accept(result);
            }
        }
    }

}
