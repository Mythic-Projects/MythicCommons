package org.mythicprojects.commons.connection.sql;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface SqlConfiguration {

    @NotNull String getUri();

    @NotNull String getUsername();

    @NotNull String getPassword();

    int getPoolSize();

    int getConnectionTimeout();

    default Optional<Integer> getIdleTimeout() {
        return Optional.empty();
    }

    default Optional<Integer> getMaxLifetime() {
        return Optional.empty();
    }

    default Optional<Integer> getKeepaliveTime() {
        return Optional.empty();
    }

}
