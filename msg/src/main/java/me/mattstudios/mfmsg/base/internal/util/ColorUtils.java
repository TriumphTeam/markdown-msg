package me.mattstudios.mfmsg.base.internal.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Floats;
import me.mattstudios.mfmsg.base.bukkit.nms.ServerVersion;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.color.handler.ColorMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public final class ColorUtils {

    private ColorUtils() {}

    public static final Set<Character> COLOR_CODES = ImmutableSet.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

    /**
     * Gets the color from a given hex code
     *
     * @param color The hex color
     * @return The new hex generated
     */
    @NotNull
    public static String ofHex(@NotNull final String color) {
        if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
            return ColorMapping.toLegacy(hexToColor(color));
        }
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

    /**
     * Turns a 3 character hex into 6 characters
     *
     * @param hex The 3 character hex color
     * @return A new 6 characters hex color
     */
    @NotNull
    public static String increaseHex(@NotNull final String hex) {
        return RegexUtils.THREE_HEX.matcher(hex).replaceAll("$1$1$2$2$3$3");
    }

    @NotNull
    public static MessageColor colorFromGradient(@NotNull final String gradient) {
        final List<String> colors = Arrays.asList(gradient.split(":"));
        if (colors.size() == 1) return new FlatColor(ColorUtils.ofHex(colors.get(0)));
        return new Gradient(colors.stream().map(ColorUtils::hexToColor).collect(Collectors.toList()));
    }

    @NotNull
    public static MessageColor colorFromRainbow(@Nullable final String satString, @Nullable final String ligString) {
        float saturation = 1.0f;
        float brightness = 1.0f;

        if (satString != null) {
            final Float sat = Floats.tryParse(satString.substring(1));
            if (sat != null && sat <= 1.0 && sat >= 0.0) {
                saturation = sat;
            }
        }

        if (ligString != null) {
            final Float light = Floats.tryParse(ligString.substring(1));
            if (light != null && light <= 1.0 && light >= 0.0) {
                brightness = light;
            }
        }

        return new Rainbow(saturation, brightness);
    }

}
