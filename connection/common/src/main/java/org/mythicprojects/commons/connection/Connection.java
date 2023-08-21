package org.mythicprojects.commons.connection;

public interface Connection {

    void open();

    void close();

    default void testConnection() throws Exception {
    }

}