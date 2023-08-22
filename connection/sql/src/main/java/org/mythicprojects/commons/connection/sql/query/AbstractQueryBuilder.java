package org.mythicprojects.commons.connection.sql.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.sql.SqlExecutable;
import org.mythicprojects.commons.util.Validate;

abstract class AbstractQueryBuilder<T extends AbstractQueryBuilder<?>> implements SqlExecutable {

    private final List<Query> queries = new ArrayList<>();

    @Contract("_ -> this")
    public T addQuery(@NotNull Query query) {
        Validate.notNull(query, "Query cannot be null");
        this.queries.add(query);
        return (T) this;
    }

    @Contract("_ -> this")
    public T addQuery(@NotNull String query) {
        return this.addQuery(new Query(query));
    }

    @Contract("_, _ -> this")
    public T addQuery(@NotNull String query, @NotNull List<Object> parameters) {
        return this.addQuery(new Query(query, parameters));
    }

    @Contract("_, _ -> this")
    public T addQuery(@NotNull String query, @NotNull Object... parameters) {
        return this.addQuery(new Query(query, parameters));
    }

    @Override
    public void execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        for (Query query : this.queries) {
            query.execute(connection, tableName);
        }
    }

}
