package org.mythicprojects.commons.replace;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Replacement {

    private final String from;

    protected Replacement(@NotNull String from) {
        this.from = from;
    }

    public @NotNull String getFrom() {
        return this.from;
    }

    public abstract @NotNull String getTo();

    public @NotNull String replace(@NotNull String text) {
        return text.replace(this.getFrom(), this.getTo());
    }

    public static @NotNull SimpleReplacement of(@NotNull String from, @Nullable Object to) {
        return new SimpleReplacement(from, to);
    }

}
