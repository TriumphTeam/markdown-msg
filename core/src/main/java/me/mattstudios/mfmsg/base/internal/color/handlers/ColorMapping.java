package me.mattstudios.mfmsg.base.internal.color.handlers;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Enum that handles legacy code conversion
 *
 * Original author: https://github.com/Esophose
 */
public enum ColorMapping {

    BLACK(0x000000, "black", '0'),
    DARK_BLUE(0x0000AA, "dark_blue", '1'),
    DARK_GREEN(0x00AA00, "dark_green", '2'),
    DARK_AQUA(0x00AAAA, "dark_aqua", '3'),
    DARK_RED(0xAA0000, "dark_red", '4'),
    DARK_PURPLE(0xAA00AA, "dark_purple", '5'),
    GOLD(0xFFAA00, "gold", '6'),
    GRAY(0xAAAAAA, "gray", '7'),
    DARK_GRAY(0x555555, "dark_gray", '8'),
    BLUE(0x5555FF, "blue", '9'),
    GREEN(0x55FF55, "green", 'a'),
    AQUA(0x55FFFF, "aqua", 'b'),
    RED(0xFF5555, "red", 'c'),
    LIGHT_PURPLE(0xFF55FF, "light_purple", 'd'),
    YELLOW(0xFFFF55, "yellow", 'e'),
    WHITE(0xFFFFFF, "white", 'f');

    private final int hex;
    private final int r, g, b;
    private final char character;
    private final String colorName;

    /**
     * @param hex       The hex code of the color
     * @param colorName The Mojang color name
     * @param character The character that represents it
     */
    ColorMapping(final int hex, @NotNull final String colorName, final char character) {
        this.hex = hex;
        this.r = (hex >> 16) & 0xFF;
        this.g = (hex >> 8) & 0xFF;
        this.b = hex & 0xFF;
        this.colorName = colorName;
        this.character = character;
    }

    /**
     * Gets the color name from a char
     *
     * @param character The char to check
     * @return The color name
     */
    @NotNull
    public static String fromChar(final char character) {
        for (ColorMapping mapping : values()) {
            if (mapping.character == character) return mapping.colorName;
        }

        return WHITE.colorName;
    }

    /**
     * Gets the color char from the name
     *
     * @param colorName The color name
     * @return The color char that it represents
     */
    public static char fromName(@NotNull final String colorName) {
        for (ColorMapping mapping : values()) {
            if (mapping.colorName.equalsIgnoreCase(colorName)) return mapping.character;
        }

        return WHITE.character;
    }

    /**
     * Gets the legacy color from a {@link Color}
     *
     * @param color The color to get from
     * @return A legacy color
     */
    @NotNull
    public static String toLegacy(@NotNull final Color color) {
        int minDist = Integer.MAX_VALUE;
        String legacy = WHITE.colorName;
        for (ColorMapping mapping : values()) {
            int r = mapping.r - color.getRed();
            int g = mapping.g - color.getGreen();
            int b = mapping.b - color.getBlue();
            int dist = r * r + g * g + b * b;
            if (dist < minDist) {
                minDist = dist;
                legacy = mapping.colorName;
            }
        }

        return legacy;
    }

    public int getHex() {
        return hex;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

}
