package org.mythicprojects.commons.connection.redis.pubsub;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.Connection;
import org.mythicprojects.commons.util.Validate;

public abstract class AbstractRedisPubSubListener<V> extends RedisPubSubAdapter<String, V> implements Connection {

    protected final String channel;

    protected AbstractRedisPubSubListener(@NotNull String channel) {
        Validate.notNull(channel, "channel cannot be null");
        this.channel = channel;
    }

    @Override
    public void message(@NotNull String channel, @NotNull V message) {
        if (!this.isChannel(channel)) {
            return;
        }

        this.onMessage(message);
    }

    public abstract void onMessage(@NotNull V message);

    public @NotNull String getChannel() {
        return this.channel;
    }

    public boolean isChannel(@NotNull String channel) {
        return this.channel.equalsIgnoreCase(channel);
    }

}
