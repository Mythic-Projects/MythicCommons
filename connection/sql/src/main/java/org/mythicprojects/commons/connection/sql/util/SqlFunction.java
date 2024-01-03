package org.mythicprojects.commons.connection.sql.util;

import java.sql.SQLException;
import org.mythicprojects.commons.function.ThrowingFunction;

@FunctionalInterface
public interface SqlFunction<T, R> extends ThrowingFunction<T, R, SQLException> {

}
