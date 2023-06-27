package org.mythicprojects.commons.replace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StringReplacer {

    private StringReplacer() {
    }

    @Contract(pure = true, value = "null, _ -> null")
    public static String replace(@Nullable String text, @NotNull Collection<? extends Replacement> replacements) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        for (Replacement replacement : replacements) {
            text = replacement.replace(text);
        }

        return text;
    }

    @Contract(pure = true, value = "null, _, -> null")
    public static String replace(@Nullable String text, @NotNull Replacement... replacements) {
        return replace(text, Arrays.asList(replacements));
    }

    @Contract(pure = true)
    public static @NotNull List<String> replace(@NotNull List<String> text, @NotNull Replacement... replacements) {
        List<String> result = new ArrayList<>();
        for (String line : text) {
            result.add(replace(line, replacements));
        }
        return result;
    }

}
