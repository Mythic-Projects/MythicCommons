package org.mythicprojects.commons.function;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);

    static <T, U, V> TriConsumer<T, U, V> empty() {
        return (t, u, v) -> {};
    }

    static <T, U, V> TriConsumer<T, U, V> fail(@NotNull RuntimeException exception) {
        return (t, u, v) -> {
            throw exception;
        };
    }

}
