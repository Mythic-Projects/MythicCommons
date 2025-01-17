package org.mythicprojects.commons.connection;

public interface Connection {

    boolean isOpen();

    void open();

    default boolean isClosed() {
        return !this.isOpen();
    }

    void close();

    default void testConnection() throws Exception {
    }

}