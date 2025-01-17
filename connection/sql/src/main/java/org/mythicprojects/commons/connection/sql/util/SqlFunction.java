package org.mythicprojects.commons.connection.sql.util;

import java.sql.SQLException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.function.ThrowingFunction;

@FunctionalInterface
public interface SqlFunction<T, R> extends ThrowingFunction<T, R, SQLException> {

    @ApiStatus.Experimental
    @Contract(value = "_ -> param1", pure = true)
    static <T, R> SqlFunction<T, R> throwing(@NotNull ThrowingFunction<T, R, ? extends Exception> function) {
        return value -> {
            try {
                return function.apply(value);
            }
            catch (Exception ex) {
                throw new SQLException(ex);
            }
        };
    }

}
