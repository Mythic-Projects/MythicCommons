package org.mythicprojects.commons.connection.sql.util;

import java.sql.SQLException;
import org.mythicprojects.commons.function.ThrowingBiFunction;

@FunctionalInterface
public interface SqlBiFunction<T, U, R> extends ThrowingBiFunction<T, U, R, SQLException> {

}
