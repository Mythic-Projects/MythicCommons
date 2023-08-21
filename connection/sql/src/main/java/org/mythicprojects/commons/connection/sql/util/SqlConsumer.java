package org.mythicprojects.commons.connection.sql.util;

import java.sql.SQLException;
import org.mythicprojects.commons.function.ThrowingConsumer;

@FunctionalInterface
public interface SqlConsumer<T> extends ThrowingConsumer<T, SQLException> {

}
