package org.mythicprojects.commons.connection.sql;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.Connection;
import org.mythicprojects.commons.connection.sql.util.DatabaseHelper;
import org.mythicprojects.commons.connection.sql.util.SqlConsumer;
import org.mythicprojects.commons.util.Validate;

public class SqlDatabase implements Connection {

    private final SqlConfiguration configuration;
    private final Consumer<HikariDataSource> dataSourceConsumer;
    private final Consumer<Throwable> exceptionHandler;
    private final Executor executor;

    private HikariDataSource dataSource;

    private final Map<String, SqlTable> tables = new ConcurrentHashMap<>();

    protected SqlDatabase(
            @NotNull SqlConfiguration configuration,
            @NotNull Consumer<HikariDataSource> dataSourceConsumer,
            @NotNull Consumer<Throwable> exceptionHandler,
            @NotNull Executor executor
    ) {
        this.configuration = Validate.notNull(configuration, "configuration cannot be null");
        this.dataSourceConsumer = Validate.notNull(dataSourceConsumer, "dataSourceConsumer cannot be null");
        this.exceptionHandler = Validate.notNull(exceptionHandler, "exceptionHandler cannot be null");
        this.executor = Validate.notNull(executor, "executor cannot be null");
    }

    @Override
    public synchronized void open() {
        if (this.dataSource != null) {
            throw new IllegalStateException("Database is already open");
        }

        this.dataSource = new HikariDataSource();

        this.dataSource.setJdbcUrl(this.configuration.getUri());
        this.dataSource.setUsername(this.configuration.getUsername());
        this.dataSource.setPassword(this.configuration.getPassword());
        this.dataSource.setMaximumPoolSize(this.configuration.getPoolSize());
        this.dataSource.setConnectionTimeout(this.configuration.getConnectionTimeout());

        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);

        this.dataSourceConsumer.accept(this.dataSource);
    }

    @Override
    public synchronized void close() {
        if (this.dataSource == null) {
            throw new IllegalStateException("Database is already closed");
        }

        this.dataSource.close();
        this.dataSource = null;
    }

    @Override
    public void testConnection() throws Exception {
        this.dataSource.getConnection().close();
    }

    @Blocking
    public void connect(@NotNull SqlConsumer<java.sql.Connection> connectionConsumer) {
        try (java.sql.Connection connection = this.dataSource.getConnection()) {
            connectionConsumer.accept(connection);
        } catch (SQLException ex) {
            this.exceptionHandler.accept(ex);
        }
    }

    @NonBlocking
    public CompletableFuture<Void> connectAsync(@NotNull SqlConsumer<java.sql.Connection> connectionConsumer) {
        return this.runAsync(() -> this.connect(connectionConsumer));
    }

    @Blocking
    public void executeStatement(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query) {
        this.connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statementConsumer.accept(statement);
            } catch (SQLException ex) {
                this.exceptionHandler.accept(ex);
            }
        });
    }

    @NonBlocking
    public CompletableFuture<Void> executeStatementAsync(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query) {
        return this.runAsync(() -> this.executeStatement(statementConsumer, query));
    }

    @Blocking
    public void executeStatement(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query, @NotNull String tableName) {
        this.executeStatement(statementConsumer, DatabaseHelper.replaceTable(query, tableName));
    }

    @NonBlocking
    public CompletableFuture<Void>  executeStatementAsync(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query, @NotNull String tableName) {
        return this.runAsync(() -> this.executeStatement(statementConsumer, query, tableName));
    }

    public Optional<SqlTable> findTable(@NotNull String tableName) {
        return Optional.ofNullable(this.tables.get(tableName));
    }

    @Blocking
    public SqlTable createTable(@NotNull String tableName, @NotNull String createQuery) {
        if (this.tables.get(tableName) != null) {
            throw new IllegalStateException("Table " + tableName + " already exists");
        }

        this.executeStatement(PreparedStatement::executeUpdate, createQuery, tableName);
        SqlTable table = new SqlTable(this, tableName);
        this.tables.put(tableName, table);
        return table;
    }

    @Blocking
    public SqlTable findOrCreateTable(@NotNull String tableName, @NotNull String createQuery) {
        return this.findTable(tableName).orElseGet(() -> this.createTable(tableName, createQuery));
    }

    @ApiStatus.Internal
    public <T> CompletableFuture<T> runAsync(@NotNull Supplier<T> consumer) {
        return CompletableFuture.supplyAsync(consumer, this.executor);
    }

    @ApiStatus.Internal
    public CompletableFuture<Void> runAsync(@NotNull Runnable consumer) {
        return CompletableFuture.runAsync(consumer, this.executor);
    }

    public static Builder builder(@NotNull SqlConfiguration configuration) {
        return new Builder(configuration);
    }

    public static final class Builder {

        private final SqlConfiguration configuration;

        private Consumer<HikariDataSource> dataSourceConsumer = dataSource -> {
        };
        private Consumer<Throwable> exceptionHandler = Throwable::printStackTrace;
        private Executor executor = ForkJoinPool.commonPool();

        private Builder(@NotNull SqlConfiguration configuration) {
            this.configuration = Validate.notNull(configuration, "configuration cannot be null");
        }

        @Contract("_ -> this")
        public Builder dataSourceConsumer(@NotNull Consumer<HikariDataSource> dataSourceConsumer) {
            this.dataSourceConsumer = Validate.notNull(dataSourceConsumer, "dataSourceConsumer cannot be null");
            return this;
        }

        @Contract("_ -> this")
        public Builder exceptionHandler(@NotNull Consumer<Throwable> exceptionHandler) {
            this.exceptionHandler = Validate.notNull(exceptionHandler, "exceptionHandler cannot be null");
            return this;
        }

        @Contract("_ -> this")
        public Builder executor(@NotNull Executor executor) {
            this.executor = Validate.notNull(executor, "executor cannot be null");
            return this;
        }

        @Contract(" -> new")
        public SqlDatabase build() {
            return new SqlDatabase(this.configuration, this.dataSourceConsumer, this.exceptionHandler, this.executor);
        }

    }

}
