package org.mythicprojects.commons.bukkit.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mythicprojects.commons.replace.Replacement;
import org.mythicprojects.commons.util.Validate;

public final class ColorUtil {

    public static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("&([0-9a-fA-Fk-oK-OrR])");
    public static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&(#[a-fA-F0-9]{6})");

    public static final Pattern LEGACY_DECOLOR_PATTERN = Pattern.compile("§([0-9A-Fa-fK-Ok-oRr])");
    public static final Pattern HEX_DECOLOR_PATTERN = Pattern.compile("§[xX]((§([0-9a-fA-F]))(§([0-9a-fA-F]))(§([0-9a-fA-F]))(§([0-9a-fA-F]))(§([0-9a-fA-F]))(§([0-9a-fA-F])))");

    private ColorUtil() {
    }

    @Contract("null -> null; _ -> !null")
    public static String color(@Nullable String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(text);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group(1);
            text = text.replace(hexMatcher.group(), ChatColor.of(hex).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Contract("null -> null")
    public static List<String> color(@Nullable Collection<String> text) {
        if (text == null) {
            return null;
        }

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return text.stream()
                .map(ColorUtil::color)
                .collect(Collectors.toList());
    }

    @Contract("null -> null")
    public static List<String> color(@Nullable String... text) {
        return color(Arrays.asList(text));
    }

    @Contract("null -> null")
    public static String[] colorArray(@Nullable String... text) {
        if (text == null) {
            return null;
        }

        return color(text).toArray(new String[text.length]);
    }

    @Contract("null -> null")
    public static String decolor(@Nullable String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        Matcher hexDecolorMatcher = HEX_DECOLOR_PATTERN.matcher(text);
        while (hexDecolorMatcher.find()) {
            String matched = hexDecolorMatcher.group();
            String simplified = Replacement.of("§", "").replace(matched.substring(2));
            text = text.replace(matched, "&#" + simplified);
        }

        return LEGACY_DECOLOR_PATTERN.matcher(text).replaceAll("&$1");
    }

    @Contract("null -> null; !null -> !null")
    public static List<String> decolor(@Nullable Collection<String> text) {
        if (text == null) {
            return null;
        }

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return text.stream()
                .map(ColorUtil::decolor)
                .collect(Collectors.toList());
    }

    @Contract("null -> null; !null -> !null")
    public static List<String> decolor(@Nullable String... text) {
        return decolor(Arrays.asList(text));
    }

    public static @NotNull ChatColor getColorFromCode(@NotNull String code) {
        Validate.notNull(code, "Code cannot be null");
        Matcher matcherLegacy = LEGACY_COLOR_PATTERN.matcher(code);
        if (matcherLegacy.find()) {
            return ChatColor.of(matcherLegacy.group(1));
        }

        Matcher matcherHex = HEX_COLOR_PATTERN.matcher(code);
        if (matcherHex.find()) {
            return ChatColor.of(matcherHex.group(1));
        }

        return ChatColor.WHITE;
    }

}
