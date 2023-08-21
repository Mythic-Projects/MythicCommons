package org.mythicprojects.commons.connection.sql;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.Connection;
import org.mythicprojects.commons.connection.sql.util.DatabaseHelper;
import org.mythicprojects.commons.connection.sql.util.SqlConsumer;
import org.mythicprojects.commons.util.Validate;

public class SqlDatabase implements Connection {

    private final SqlConfiguration configuration;
    private final Consumer<Throwable> exceptionHandler;
    private final Consumer<Runnable> runAsync;

    private HikariDataSource dataSource;

    private final Map<String, SqlTable> tables = new ConcurrentHashMap<>();

    public SqlDatabase(@NotNull SqlConfiguration configuration, @NotNull Consumer<Throwable> exceptionHandler, @NotNull Consumer<Runnable> runAsync) {
        this.configuration = Validate.notNull(configuration, "configuration cannot be null");
        this.exceptionHandler = Validate.notNull(exceptionHandler, "exceptionHandler cannot be null");
        this.runAsync = Validate.notNull(runAsync, "runAsync cannot be null");
    }

    public @NotNull String prepareTableName(@NotNull String baseName) {
        return this.configuration.getTablePrefix() + baseName;
    }

    @Override
    public void open() {
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
    }

    @Override
    public void close() {
        this.dataSource.close();
    }

    @Override
    public void testConnection() throws Exception {
        this.dataSource.getConnection().close();
    }

    public void connect(@NotNull SqlConsumer<java.sql.Connection> connectionConsumer) {
        try (java.sql.Connection connection = this.dataSource.getConnection()) {
            connectionConsumer.accept(connection);
        } catch (SQLException ex) {
            this.exceptionHandler.accept(ex);
        }
    }

    public void connectAsync(@NotNull SqlConsumer<java.sql.Connection> connectionConsumer) {
        this.runAsync(() -> this.connect(connectionConsumer));
    }

    public void executeStatement(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query) {
        this.connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statementConsumer.accept(statement);
            } catch (SQLException ex) {
                this.exceptionHandler.accept(ex);
            }
        });
    }

    public void executeStatementAsync(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query) {
        this.runAsync(() -> this.executeStatement(statementConsumer, query));
    }

    public void executeStatement(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query, @NotNull String tableName) {
        this.executeStatement(statementConsumer, DatabaseHelper.replaceTable(query, tableName));
    }

    public void executeStatementAsync(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query, @NotNull String tableName) {
        this.runAsync(() -> this.executeStatement(statementConsumer, query, tableName));
    }

    public void executeStatementWithPreparedTable(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query, @NotNull String baseTableName) {
        this.executeStatement(statementConsumer, query, this.prepareTableName(baseTableName));
    }

    public void executeStatementWithPreparedTableAsync(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query, @NotNull String baseTableName) {
        this.runAsync(() -> this.executeStatementWithPreparedTable(statementConsumer, query, baseTableName));
    }

    public Optional<SqlTable> findTable(@NotNull String baseName) {
        return Optional.ofNullable(this.tables.get(this.prepareTableName(baseName)));
    }

    @Blocking
    public SqlTable createTable(@NotNull String baseName, @NotNull String createQuery) {
        String tableName = this.prepareTableName(baseName);
        if (this.tables.get(tableName) != null) {
            throw new IllegalStateException("Table " + tableName + " already exists");
        }

        this.executeStatement(PreparedStatement::executeUpdate, createQuery, tableName);
        SqlTable table = new SqlTable(this, tableName);
        this.tables.put(tableName, table);
        return table;
    }

    @Blocking
    public SqlTable findOrCreateTable(@NotNull String baseName, @NotNull String createQuery) {
        return this.findTable(baseName).orElseGet(() -> this.createTable(baseName, createQuery));
    }

    public void runAsync(@NotNull Runnable runnable) {
        this.runAsync.accept(runnable);
    }

}
