package org.mythicprojects.commons.connection.redis.pubsub;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.redis.RedisConnection;

/**
 * Simple implementation of {@link SimpleRedisPubSubListener} that processes messages asynchronously.
 */
public abstract class AsyncRedisPubSubListener extends SimpleRedisPubSubListener {

    private final Consumer<Runnable> runAsync;

    public AsyncRedisPubSubListener(@NotNull String channel, @NotNull RedisConnection connection) {
        super(channel, connection.getClient());
        this.runAsync = connection::runAsync;
    }

    @Override
    public final void onMessage(@NotNull String message) {
        this.runAsync.accept(() -> this.onMessageAsync(message));
    }

    public abstract void onMessageAsync(@NotNull String message);

}
