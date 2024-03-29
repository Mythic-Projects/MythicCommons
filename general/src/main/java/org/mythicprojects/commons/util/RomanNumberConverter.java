package org.mythicprojects.commons.util;

import java.util.TreeMap;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for converting arabic numbers to roman numbers
 */
public final class RomanNumberConverter {

    private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();
    private static final TreeMap<Integer, String> ROMAN_BASE = new TreeMap<>();

    static {
        ROMAN_BASE.put(1000, "M");
        ROMAN_BASE.put(900, "CM");
        ROMAN_BASE.put(500, "D");
        ROMAN_BASE.put(400, "CD");
        ROMAN_BASE.put(100, "C");
        ROMAN_BASE.put(90, "XC");
        ROMAN_BASE.put(50, "L");
        ROMAN_BASE.put(40, "XL");
        ROMAN_BASE.put(10, "X");
        ROMAN_BASE.put(9, "IX");
        ROMAN_BASE.put(5, "V");
        ROMAN_BASE.put(4, "IV");
        ROMAN_BASE.put(1, "I");
    }

    private RomanNumberConverter() {
    }

    /**
     * Converts arabic number to roman number
     *
     * @param arabic number to convert
     * @return roman number
     */
    public static @NotNull String toRoman(int arabic) {
        if (arabic <= 0) {
            return "0";
        }
        String roman = ROMAN_NUMERALS.get(arabic);
        if (roman == null) {
            int floor = ROMAN_BASE.floorKey(arabic);
            roman = ROMAN_BASE.get(floor) + (floor == arabic
                    ? ""
                    : toRoman(arabic - floor));
            ROMAN_NUMERALS.put(arabic, roman);
        }
        return roman;
    }

}
