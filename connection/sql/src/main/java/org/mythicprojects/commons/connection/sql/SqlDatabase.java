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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.mythicprojects.commons.connection.Connection;
import org.mythicprojects.commons.connection.sql.util.DatabaseHelper;
import org.mythicprojects.commons.connection.sql.util.SqlFunction;
import org.mythicprojects.commons.util.Validate;

public class SqlDatabase implements Connection {

    private final SqlConfiguration configuration;
    private final Consumer<HikariDataSource> dataSourceConsumer;
    private final Consumer<Throwable> exceptionHandler;
    private final Executor executor;

    private HikariDataSource dataSource;

    private final Map<String, SqlTable> tables = new ConcurrentHashMap<>();

    private final AtomicBoolean wasOpen = new AtomicBoolean(false);

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
    public boolean isOpen() {
        return this.dataSource != null;
    }

    @Override
    public synchronized void open() {
        if (this.isOpen()) {
            throw new IllegalStateException("Database is already open.");
        }
        else if (this.wasOpen.get()) {
            throw new IllegalStateException("Database is closed. Create a new database to open it.");
        }

        this.dataSource = new HikariDataSource();

        this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.dataSource.setJdbcUrl(this.configuration.getUri());
        this.dataSource.setUsername(this.configuration.getUsername());
        this.dataSource.setPassword(this.configuration.getPassword());
        this.dataSource.setMaximumPoolSize(this.configuration.getPoolSize());
        this.dataSource.setConnectionTimeout(this.configuration.getConnectionTimeout());

        this.configuration.getIdleTimeout().ifPresent(value -> this.dataSource.setIdleTimeout(value));
        this.configuration.getMaxLifetime().ifPresent(value -> this.dataSource.setMaxLifetime(value));
        this.configuration.getKeepaliveTime().ifPresent(value -> this.dataSource.setKeepaliveTime(value));

        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);

        this.dataSourceConsumer.accept(this.dataSource);

        this.wasOpen.set(true);
    }

    @Override
    public synchronized void close() {
        if (this.isClosed()) {
            throw new IllegalStateException("Database is already closed.");
        }

        this.tables.clear();
        this.dataSource.close();
        this.dataSource = null;
    }

    @Override
    public void testConnection() throws Exception {
        this.dataSource.getConnection().close();
    }

    @Blocking
    public <T> @UnknownNullability T connect(@NotNull SqlFunction<java.sql.Connection, T> function) {
        try (java.sql.Connection connection = this.dataSource.getConnection()) {
            return function.apply(connection);
        }
        catch (SQLException ex) {
            this.exceptionHandler.accept(ex);
            return null;
        }
    }

    @NonBlocking
    public <T> CompletableFuture<@UnknownNullability T> connectAsync(@NotNull SqlFunction<java.sql.Connection, T> function) {
        return this.runAsync(() -> this.connect(function));
    }

    @Blocking
    public <T> @UnknownNullability T executeStatement(@NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query) {
        return this.connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                return function.apply(statement);
            }
            catch (SQLException ex) {
                this.exceptionHandler.accept(ex);
                return null;
            }
        });
    }

    @NonBlocking
    public <T> CompletableFuture<@UnknownNullability T> executeStatementAsync(
            @NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query
    ) {
        return this.runAsync(() -> this.executeStatement(function, query));
    }

    @Blocking
    public <T> @UnknownNullability T executeStatement(
            @NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query, @NotNull String tableName
    ) {
        return this.executeStatement(function, DatabaseHelper.replaceTable(query, tableName));
    }

    @NonBlocking
    public <T> CompletableFuture<@UnknownNullability T> executeStatementAsync(
            @NotNull SqlFunction<PreparedStatement, T> function, @NotNull String query, @NotNull String tableName
    ) {
        return this.executeStatementAsync(function, DatabaseHelper.replaceTable(query, tableName));
    }

    public Optional<SqlTable> findTable(@NotNull String tableName) {
        return Optional.ofNullable(this.tables.get(tableName));
    }

    @Blocking
    public @NotNull SqlTable createTable(@NotNull String tableName, @NotNull String createQuery) {
        if (this.tables.get(tableName) != null) {
            throw new IllegalStateException("Table " + tableName + " already exists");
        }

        this.executeStatement(PreparedStatement::executeUpdate, createQuery, tableName);
        SqlTable table = new SqlTable(this, tableName);
        this.tables.put(tableName, table);
        return table;
    }

    @Blocking
    public @NotNull SqlTable findOrCreateTable(@NotNull String tableName, @NotNull String createQuery) {
        return this.findTable(tableName).orElseGet(() -> this.createTable(tableName, createQuery));
    }

    @ApiStatus.Internal
    public final <T> CompletableFuture<T> runAsync(@NotNull Supplier<T> consumer) {
        return CompletableFuture.supplyAsync(consumer, this.executor);
    }

    @Contract("_ -> new")
    public static Builder builder(@NotNull SqlConfiguration configuration) {
        return new Builder(configuration);
    }

    public static final class Builder {

        private final SqlConfiguration configuration;

        private Consumer<HikariDataSource> dataSourceConsumer = dataSource -> {};
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
