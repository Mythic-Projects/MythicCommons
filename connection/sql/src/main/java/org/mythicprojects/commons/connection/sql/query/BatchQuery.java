package org.mythicprojects.commons.connection.sql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;
import org.mythicprojects.commons.connection.sql.SqlExecutable;
import org.mythicprojects.commons.connection.sql.util.DatabaseHelper;
import org.mythicprojects.commons.util.Validate;

public class BatchQuery<T> implements SqlExecutable<T> {

    private static final int DEFAULT_BATCH_SIZE = 100;

    private final String query;
    private final List<List<Object>> parametersList;
    private final int batchSize;

    public BatchQuery(@NotNull String query, @NotNull List<List<Object>> parametersList, int batchSize) {
        this.query = Validate.notEmpty(query, "query cannot be null or empty");
        this.parametersList = validateAndPrepareParametersList(parametersList);
        Validate.isTrue(batchSize > 0, "batchSize must be greater than 0");
        this.batchSize = batchSize;
    }

    public BatchQuery(@NotNull String query, @NotNull List<List<Object>> parametersList) {
        this(query, parametersList, DEFAULT_BATCH_SIZE);
    }

    public @NotNull String getQuery() {
        return this.query;
    }

    public @NotNull @Unmodifiable List<@Unmodifiable List<Object>> getParametersList() {
        return this.parametersList;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    @Override
    public @UnknownNullability T execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DatabaseHelper.replaceTable(this.query, tableName))) {
            int currentBatchSize = 0;
            for (List<Object> parameters : this.parametersList) {
                DatabaseHelper.completeStatement(statement, parameters);
                statement.addBatch();
                currentBatchSize++;
                if (currentBatchSize >= this.batchSize) {
                    statement.executeBatch();
                    currentBatchSize = 0;
                }
            }
            if (currentBatchSize > 0) {
                // Execute the remaining batch
                statement.executeBatch();
            }
            return null;
        }
    }

    @NotNull
    private static List<List<Object>> validateAndPrepareParametersList(@NotNull List<List<Object>> parametersList) {
        Validate.notNull(parametersList, "parameters cannot be null");
        List<List<Object>> finalParametersList = new ArrayList<>();
        boolean allParametersNotNull = true;
        boolean allParametersSameSize = true;
        int parametersSize = -1;
        for (List<Object> parameter : parametersList) {
            if (parameter == null) {
                allParametersNotNull = false;
                break;
            }

            if (parametersSize == -1) {
                parametersSize = parameter.size();
            } else if (parametersSize != parameter.size()) {
                allParametersSameSize = false;
                break;
            }

            finalParametersList.add(List.copyOf(parameter));
        }
        Validate.isTrue(allParametersNotNull, "parameters cannot contain null elements");
        Validate.isTrue(allParametersSameSize, "parameters must have the same size");
        return finalParametersList;
    }

}
