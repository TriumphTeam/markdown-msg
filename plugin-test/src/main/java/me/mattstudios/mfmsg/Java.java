package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.base.internal.util.ColorUtils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Java {

    public static List<Color> stream(final List<String> colors) {
        return colors.stream().map(ColorUtils::hexToColor).collect(Collectors.toList());
    }

    public static List<Color> normal(final List<String> colors) {
        final List<Color> colorList = new ArrayList<>();
        for (final String color : colors) {
            colorList.add(ColorUtils.hexToColor(color));
        }
        return colorList;
    }

}
