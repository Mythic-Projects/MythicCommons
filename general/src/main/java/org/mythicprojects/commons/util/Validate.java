package org.mythicprojects.commons.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public final class Validate {

    private Validate() {
    }

    public static <T> @NotNull T notNull(@UnknownNullability T object, @NotNull String message, @NotNull Object... args) {
        return Objects.requireNonNull(object, String.format(message, args));
    }

    public static <T> @NotNull T notNull(@UnknownNullability T object, @NotNull Object... args) {
        return notNull(object, "Object cannot be null", args);
    }

    public static void isNull(@UnknownNullability Object object, @NotNull String message, @NotNull Object... args) {
        if (object != null) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void isNull(@UnknownNullability Object object, @NotNull Object... args) {
        isNull(object, "Object must be null", args);
    }

    public static void isTrue(boolean condition, @NotNull String message, @NotNull Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void isTrue(boolean condition, @NotNull Object... args) {
        isTrue(condition, "Condition is not true", args);
    }

    public static void isFalse(boolean condition, @NotNull String message, @NotNull Object... args) {
        if (condition) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void isFalse(boolean condition, @NotNull Object... args) {
        isFalse(condition, "Condition is not false", args);
    }

    public static @NotNull String notEmpty(@UnknownNullability String string, @NotNull String message, @NotNull Object... args) {
        notNull(string, message, args);
        if (string.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return string;
    }

    public static @NotNull String notEmpty(@UnknownNullability String string, @NotNull Object... args) {
        return notEmpty(string, "String cannot be empty", args);
    }

    public static @NotNull String notBlank(@UnknownNullability String string, @NotNull String message, @NotNull Object... args) {
        notNull(string, message, args);
        notEmpty(string, message, args);
        if (string.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format(message, args));
        }
        return string;
    }

    public static @NotNull String notBlank(@UnknownNullability String string, @NotNull Object... args) {
        return notBlank(string, "String cannot be empty or blank", args);
    }

}
