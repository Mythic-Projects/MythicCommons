package org.mythicprojects.commons.connection.sql;

import org.jetbrains.annotations.NotNull;

public interface SqlConfiguration {

    @NotNull String getUri();

    @NotNull String getUsername();

    @NotNull String getPassword();

    int getPoolSize();

    int getConnectionTimeout();

    int getIdleTimeout();

    int getMaxLifetime();

    int getKeepaliveTime();

}
