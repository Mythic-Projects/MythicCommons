package org.mythicprojects.commons.connection.redis.pubsub;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleRedisPubSubListener extends AbstractRedisPubSubListener<String> {

    protected final RedisClient client;

    protected StatefulRedisPubSubConnection<String, String> connection;

    public SimpleRedisPubSubListener(@NotNull String channel, @NotNull RedisClient client) {
        super(channel);
        this.client = client;
    }

    @Override
    public boolean isOpen() {
        return this.connection != null && this.connection.isOpen();
    }

    @Override
    public void open() {
        this.connection = this.client.connectPubSub();
        this.connection.addListener(this);
        this.connection.sync().subscribe(this.channel);
    }

    @Override
    public void close() {
        if (this.connection == null || !this.connection.isOpen()) {
            return;
        }
        this.connection.sync().unsubscribe(this.channel);
        this.connection.close();
    }

}
