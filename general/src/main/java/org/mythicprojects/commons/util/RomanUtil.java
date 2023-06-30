package org.mythicprojects.commons.util;

import java.util.TreeMap;
import org.jetbrains.annotations.NotNull;

public final class RomanUtil {

    private static final TreeMap<Long, String> ROMAN_NUMERALS = new TreeMap<>();
    private static final TreeMap<Long, String> ROMAN_BASE = new TreeMap<>();

    static {
        ROMAN_BASE.put(1000L, "M");
        ROMAN_BASE.put(900L, "CM");
        ROMAN_BASE.put(500L, "D");
        ROMAN_BASE.put(400L, "CD");
        ROMAN_BASE.put(100L, "C");
        ROMAN_BASE.put(90L, "XC");
        ROMAN_BASE.put(50L, "L");
        ROMAN_BASE.put(40L, "XL");
        ROMAN_BASE.put(10L, "X");
        ROMAN_BASE.put(9L, "IX");
        ROMAN_BASE.put(5L, "V");
        ROMAN_BASE.put(4L, "IV");
        ROMAN_BASE.put(1L, "I");
    }

    private RomanUtil() {
    }

    /**
     * Converts arabic number to roman number
     *
     * @param arabic number to convert
     * @return roman number
     */
    public static @NotNull String toRoman(long arabic) {
        if (arabic <= 0) {
            return "0";
        }
        String roman = ROMAN_NUMERALS.get(arabic);
        if (roman == null) {
            long floor = ROMAN_BASE.floorKey(arabic);
            roman = ROMAN_BASE.get(floor) + (floor == arabic
                    ? ""
                    : toRoman(arabic - floor));
            ROMAN_NUMERALS.put(arabic, roman);
        }
        return roman;
    }

}
