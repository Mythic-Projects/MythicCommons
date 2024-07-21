package org.mythicprojects.commons.connection.sql.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.mythicprojects.commons.connection.sql.SqlExecutable;
import org.mythicprojects.commons.util.Validate;

@SuppressWarnings("unchecked")
abstract class AbstractQueryBuilder<T extends AbstractQueryBuilder<?>> implements SqlExecutable<Map<Query<?>, Object>> {

    private final Queue<Query<?>> queries = new ConcurrentLinkedQueue<>();

    @Contract("_ -> this")
    public T addQuery(@NotNull Query<?> query) {
        Validate.notNull(query, "Query cannot be null");
        this.queries.add(query);
        return (T) this;
    }

    @Contract("_ -> this")
    public T addQuery(@NotNull Iterable<Query<?>> queries) {
        for (Query<?> query : queries) {
            this.addQuery(query);
        }
        return (T) this;
    }

    @Contract("_ -> this")
    public T addQuery(@NotNull String query) {
        return this.addQuery(new Query<>(query));
    }

    @Contract("_, _ -> this")
    public T addQuery(@NotNull String query, @NotNull List<Object> parameters) {
        return this.addQuery(new Query<>(query, parameters));
    }

    @Contract("_, _ -> this")
    public T addQuery(@NotNull String query, @NotNull Object... parameters) {
        return this.addQuery(new Query<>(query, parameters));
    }

    @Override
    public @NotNull @Unmodifiable Map<Query<?>, Object> execute(@NotNull Connection connection, @NotNull String tableName) throws SQLException {
        Map<Query<?>, Object> results = new LinkedHashMap<>();
        while (!this.queries.isEmpty()) {
            Query<?> query = this.queries.poll();
            Object result = query.execute(connection, tableName);
            results.put(query, result);
        }
        return Collections.unmodifiableMap(results);
    }

}
