package me.mattstudios.mfmsg.base.internal.color.handler;

import com.google.common.primitives.Floats;
import me.mattstudios.mfmsg.base.bukkit.nms.ServerVersion;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Class to handle all color related parts of the massage
 */
@SuppressWarnings("UnstableApiUsage")
public final class ColorHandler {

    private final Set<Format> formats;
    private final MessageColor defaultColor;

    // Keeps the current color so colors can pass to other parts
    private MessageColor currentColor;

    /**
     * Main constructor of the handler that takes the formats and a default color
     *
     * @param formats      The allowed {@link Format}s to use
     * @param defaultColor Default {@link MessageColor} if one is introduced
     */
    public ColorHandler(@NotNull final Set<Format> formats, @NotNull final MessageColor defaultColor) {
        this.formats = formats;
        this.defaultColor = defaultColor;

        currentColor = defaultColor;
    }

    /**
     * Colorizes a message
     *
     * @param message    The message to check for colors
     * @param bold       Whether or not the message is bold
     * @param italic     Whether or not the message is italic
     * @param strike     Whether or not the message is strikethrough
     * @param underline  Whether or not the message is underlined
     * @param obfuscated Whether or not the message is obfuscated
     * @param actions    All the actions the message has
     * @return A list with the current {@link MessagePart}s
     */
    @NotNull
    public List<MessagePart> colorize(@NotNull String message, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @NotNull final List<Action> actions) {
        final List<MessagePart> parts = new ArrayList<>();
        final Matcher matcher = RegexUtils.COLOR_PATTERN.matcher(message);

        String rest = message;
        int start = 0;

        // Loops through the matches
        while (matcher.find()) {

            // Gets the message before the match and adds it
            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                parts.add(new MessagePart(before, currentColor, bold, italic, strike, underline, obfuscated, actions));
            }

            // Looks for legacy colors and stopping points
            final String colorChar = matcher.group("char");
            if (colorChar != null) {
                if (formats.contains(Format.COLOR)) {
                    currentColor = new FlatColor(ColorMapping.fromChar(colorChar.charAt(0)));
                } else {
                    if ("r".equalsIgnoreCase(colorChar)) currentColor = defaultColor;
                }
            }

            // Looks for hex colors
            final String hex = matcher.group("hex");
            if (hex != null && formats.contains(Format.HEX)) {
                currentColor = new FlatColor(ofHex(hex));
            }

            // Looks for gradient
            final String gradient = matcher.group("gradient");
            if (gradient != null && !ServerVersion.CURRENT_VERSION.isColorLegacy() && formats.contains(Format.GRADIENT)) {
                final List<String> colors = Arrays.asList(gradient.split(":"));
                if (colors.size() == 1) currentColor = new FlatColor(ofHex(colors.get(0)));
                else currentColor = new Gradient(colors.stream().map(this::hexToColor).collect(Collectors.toList()));
            }

            // Looks for rainbow
            if (matcher.group("r") != null && !ServerVersion.CURRENT_VERSION.isColorLegacy() && formats.contains(Format.RAINBOW)) {
                final String satString = matcher.group("sat");
                final String ligString = matcher.group("lig");

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

                currentColor = new Rainbow(saturation, brightness);
            }

            // Sets the rest of the message and the start for the new one
            start = matcher.end();
            rest = message.substring(start);

        }

        // In case there is messages left that doesn't fit in the matcher adds them
        if (!rest.isEmpty()) {
            parts.add(new MessagePart(rest, currentColor, bold, italic, strike, underline, obfuscated, actions));
        }

        return parts;
    }

    /**
     * Gets the color from a given hex code
     *
     * @param color The hex color
     * @return The new hex generated
     */
    @NotNull
    private String ofHex(@NotNull final String color) {
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
    private Color hexToColor(@NotNull final String color) {
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
    private String increaseHex(@NotNull final String hex) {
        return RegexUtils.THREE_HEX.matcher(hex).replaceAll("$1$1$2$2$3$3");
    }

}
