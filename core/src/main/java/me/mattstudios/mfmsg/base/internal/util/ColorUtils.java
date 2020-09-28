package me.mattstudios.mfmsg.base.internal.util;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
public final class ColorUtils {

    private ColorUtils() {}

    // Pattern for turning #000 into #000000
    private static final Pattern THREE_HEX = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");

    /**
     * Gets the color from a given hex code
     *
     * @param color The hex color
     * @return The new hex generated
     */
    @NotNull
    public static String ofHex(@NotNull final String color) {
        return "#" + Integer.toHexString(hexToColor(color).getRGB()).substring(2);
    }

    /**
     * Turns a hex string into a {@link Color} from either 3 or 6 hex characters
     *
     * @param color The hex color
     * @return A new {@link Color}
     */
    @NotNull
    public static Color hexToColor(@NotNull final String color) {
        final StringBuilder builder = new StringBuilder();
        final String hex = color.substring(1);

        builder.append("#");
        if (hex.length() == 6) {
            builder.append(hex);
        } else {
            builder.append(increaseHex(hex.substring(0, 3)));
        }

        return Color.decode(builder.toString());
    }

    public static List<Color> hexToColorList(@NotNull final List<String> colors) {
        final List<Color> colorList = new ArrayList<>();
        for (final String color : colors) {
            colorList.add(hexToColor(color));
        }
        return colorList;
    }

    /**
     * Turns a 3 character hex into 6 characters
     *
     * @param hex The 3 character hex color
     * @return A new 6 characters hex color
     */
    @NotNull
    public static String increaseHex(@NotNull final String hex) {
        return THREE_HEX.matcher(hex).replaceAll("$1$1$2$2$3$3");
    }

}
