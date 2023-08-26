package org.mythicprojects.commons.function;

import java.util.function.Supplier;
import org.mythicprojects.commons.wrapper.Pair;

@FunctionalInterface
public interface ThrowingBiSupplier <T, U, E extends Exception> {

    Pair<T, U> get() throws E;

    static <T, U, E extends Exception> ThrowingBiSupplier<T, U, E> empty() {
        return () -> null;
    }

    static <T, U, E extends Exception> ThrowingBiSupplier<T, U, E> fail(Supplier<? extends E> exceptionSupplier) {
        return () -> {
            throw exceptionSupplier.get();
        };
    }

}
