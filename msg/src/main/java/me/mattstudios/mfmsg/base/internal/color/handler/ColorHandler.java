package me.mattstudios.mfmsg.base.internal.color.handler;

import me.mattstudios.mfmsg.base.bukkit.nms.ServerVersion;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.util.ColorUtils;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Class to handle all color related parts of the massage
 */
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

            // Gets the mes sage before the match and adds it
            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) {
                parts.add(new MessagePart(before, currentColor, bold, italic, strike, underline, obfuscated, actions));
            }

            // Looks for legacy colors and stopping points
            final String colorChar = matcher.group("char");
            if (colorChar != null) {
                if (ColorUtils.COLOR_CODES.contains(colorChar.charAt(0)) && formats.contains(Format.COLOR)) {
                    currentColor = new FlatColor(ColorMapping.fromChar(colorChar.charAt(0)));
                } else {
                    if ("r".equalsIgnoreCase(colorChar)) currentColor = defaultColor;
                }
            }

            // Looks for hex colors
            final String hex = matcher.group("hex");
            if (hex != null && formats.contains(Format.HEX)) {
                currentColor = new FlatColor(ColorUtils.ofHex(hex));
            }

            // Looks for gradient
            final String gradient = matcher.group("gradient");
            if (gradient != null && !ServerVersion.CURRENT_VERSION.isColorLegacy() && formats.contains(Format.GRADIENT)) {
                currentColor = ColorUtils.colorFromGradient(gradient);
            }

            // Looks for rainbow
            if (matcher.group("r") != null && !ServerVersion.CURRENT_VERSION.isColorLegacy() && formats.contains(Format.RAINBOW)) {
                currentColor = ColorUtils.colorFromRainbow(matcher.group("sat"), matcher.group("lig"));
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

}
