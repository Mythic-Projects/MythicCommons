package org.mythicprojects.commons.connection.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.Connection;

public class RedisConnection implements Connection {

    private final RedisConfiguration configuration;
    private final Consumer<Runnable> runAsync;

    private RedisURI uri;
    private RedisClient client;

    private RedisRepository repository;

    public RedisConnection(@NotNull RedisConfiguration configuration, @NotNull Consumer<Runnable> runAsync) {
        this.configuration = configuration;
        this.runAsync = runAsync;
    }

    public @NotNull RedisURI getUri() {
        return this.uri;
    }

    public @NotNull RedisClient getClient() {
        return this.client;
    }

    public @NotNull RedisRepository getRepository() {
        return this.repository;
    }

    public @NotNull StatefulRedisConnection<String, String> connect() {
        return this.client.connect();
    }

    public void connect(@NotNull Consumer<StatefulRedisConnection<String, String>> consumer) {
        try (StatefulRedisConnection<String, String> connection = this.connect()) {
            consumer.accept(connection);
        }
    }

    public void connectAsync(@NotNull Consumer<StatefulRedisConnection<String, String>> consumer) {
        this.runAsync(() -> this.connect(consumer));
    }

    public void connectPubSub(@NotNull Consumer<StatefulRedisPubSubConnection<String, String>> consumer) {
        try (StatefulRedisPubSubConnection<String, String> connection = this.client.connectPubSub()) {
            consumer.accept(connection);
        }
    }

    public void connectPubSubAsync(@NotNull Consumer<StatefulRedisPubSubConnection<String, String>> consumer) {
        this.runAsync(() -> this.connectPubSub(consumer));
    }

    @Override
    public void open() {
        this.uri = RedisURI.create(this.configuration.getUri());
        this.client = RedisClient.create(this.uri);

        this.repository = new RedisRepository(this);
    }

    @Override
    public void close() {
        this.repository.closeConnections();
        this.client.shutdown();
    }

    @Override
    public void testConnection() throws RedisConnectionException {
        StatefulRedisConnection<String, String> connection = this.connect();
        connection.sync().ping();
        connection.close();
    }

    public void runAsync(@NotNull Runnable runnable) {
        this.runAsync.accept(runnable);
    }

}
