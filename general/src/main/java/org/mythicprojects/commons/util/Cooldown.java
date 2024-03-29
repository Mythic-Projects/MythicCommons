package org.mythicprojects.commons.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.WeakHashMap;
import org.jetbrains.annotations.NotNull;

public final class Cooldown<T> {

    private final Map<T, Instant> cooldowns = new WeakHashMap<>(32);

    /**
     * @param key the key to check
     * @return true if the key is on cooldown, false otherwise
     */
    public boolean isOnCooldown(@NotNull T key) {
        Instant cooldown = this.cooldowns.get(key);
        if (cooldown == null) {
            return false;
        }

        if (cooldown.isAfter(Instant.now())) {
            return true;
        }

        this.cooldowns.remove(key);
        return false;
    }

    /**
     * @param key the key to check
     * @return the remaining cooldown, or zero if the key is not on cooldown
     */
    public @NotNull Duration getRemainingCooldown(@NotNull T key) {
        Instant cooldown = this.cooldowns.get(key);
        if (cooldown == null) {
            return Duration.ZERO;
        }

        if (cooldown.isAfter(Instant.now())) {
            return Duration.between(Instant.now(), cooldown);
        }

        this.cooldowns.remove(key);
        return Duration.ZERO;
    }

    /**
     * Puts the key on cooldown for the specified duration
     * @param key the key to put on cooldown
     * @param duration the duration of the cooldown
     */
    public void putOnCooldown(@NotNull T key, @NotNull Duration duration) {
        this.cooldowns.put(key, Instant.now().plus(duration));
    }

    /**
     * Puts the key on cooldown for the specified duration if it is not already on cooldown
     * @param key the key to put on cooldown
     * @param cooldown the cooldown
     * @return true if the key was on cooldown, false otherwise
     */
    public boolean cooldown(@NotNull T key, @NotNull Duration cooldown) {
        if (this.isOnCooldown(key)) {
            return true;
        }

        this.putOnCooldown(key, cooldown);
        return false;
    }

}