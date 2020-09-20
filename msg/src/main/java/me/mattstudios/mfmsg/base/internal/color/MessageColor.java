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

    @NotNull
    static MessageColor of(final char color) {
        return new FlatColor(ColorMapping.fromChar(color));
    }

    @NotNull
    static MessageColor of(final String color) {
        return new FlatColor(color);
    }

    @NotNull
    static MessageColor of(final float saturation, final float brightness) {
        return new Rainbow(saturation, brightness);
    }

    @NotNull
    static MessageColor of(final List<String> colors) {
        if (colors.size() == 1) return new FlatColor(ColorUtils.ofHex(colors.get(0)));
        return new Gradient(colors.stream().map(ColorUtils::hexToColor).collect(Collectors.toList()));
    }

}
