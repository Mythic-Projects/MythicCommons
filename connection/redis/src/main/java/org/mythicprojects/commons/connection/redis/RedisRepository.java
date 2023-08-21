package org.mythicprojects.commons.connection.redis;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.redis.pubsub.AbstractRedisPubSubListener;
import org.mythicprojects.commons.util.Validate;

public class RedisRepository {

    private final RedisConnection redisConnection;
    private final Map<String, AbstractRedisPubSubListener<?>> pubSubListenerMap = new ConcurrentHashMap<>();

    RedisRepository(@NotNull RedisConnection redisConnection) {
        Validate.notNull(redisConnection, "redisConnection cannot be null");
        this.redisConnection = redisConnection;
    }

    public void registerPubSub(@NotNull String id, @NotNull AbstractRedisPubSubListener<?> listener) {
        AbstractRedisPubSubListener<?> oldListener = this.pubSubListenerMap.put(id.toLowerCase(Locale.ROOT), listener);
        if (oldListener != null) {
            oldListener.close();
        }
        listener.open();
    }

    public void registerPubSub(@NotNull AbstractRedisPubSubListener<?> listener) {
        this.registerPubSub(listener.getChannel(), listener);
    }

    public void registerPubSub(@NotNull String id, @NotNull Function<RedisConnection, AbstractRedisPubSubListener<?>> createListener) {
        this.registerPubSub(id, createListener.apply(this.redisConnection));
    }

    public void registerPubSub(@NotNull Function<RedisConnection, AbstractRedisPubSubListener<?>> createListener) {
        AbstractRedisPubSubListener<?> listener = createListener.apply(this.redisConnection);
        this.registerPubSub(listener.getChannel(), listener);
    }

    public Optional<AbstractRedisPubSubListener<?>> findPubSubListener(@NotNull String id) {
        return Optional.ofNullable(this.pubSubListenerMap.get(id.toLowerCase(Locale.ROOT)));
    }

    public void closeConnections() {
        this.pubSubListenerMap.values().forEach(AbstractRedisPubSubListener::close);
    }

}
