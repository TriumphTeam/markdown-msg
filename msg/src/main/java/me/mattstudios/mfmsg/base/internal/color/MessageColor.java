package me.mattstudios.mfmsg.base.internal.color;

import me.mattstudios.mfmsg.base.internal.color.handler.ColorMapping;
import me.mattstudios.mfmsg.base.internal.util.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Common interface for the message color types
 */
public interface MessageColor {

    /**
     * Gets a MessageColor from the given char
     *
     * @param color The color Char
     * @return A FlatColor from the char
     */
    @NotNull
    static MessageColor from(final char color) {
        return new FlatColor(ColorMapping.fromChar(color));
    }

    /**
     * Gets a MessageColor from a given String (hex code)
     *
     * @param color The hex color
     * @return A FlatColor from the given Hex
     */
    @NotNull
    static MessageColor from(final String color) {
        return new FlatColor(color);
    }

    /**
     * Gets a MessageColor from a given saturation and brightness
     *
     * @param saturation The saturation
     * @param brightness The brightness
     * @return A Rainbow color
     */
    @NotNull
    static MessageColor from(final float saturation, final float brightness) {
        return new Rainbow(saturation, brightness);
    }

    /**
     * Gets a MessageColor from a given List of hexes
     *
     * @param colors The list of hexes
     * @return A Gradient color
     */
    @NotNull
    static MessageColor from(final List<String> colors) {
        if (colors.size() == 1) return new FlatColor(ColorUtils.ofHex(colors.get(0)));
        return new Gradient(colors.stream().map(ColorUtils::hexToColor).collect(Collectors.toList()));
    }

}
