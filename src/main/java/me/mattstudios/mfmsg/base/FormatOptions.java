package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class FormatOptions {

    private final Set<Format> formats;
    private MessageColor defaultColor = new FlatColor("white");

    FormatOptions() {
        this.formats = EnumSet.allOf(Format.class);
    }

    FormatOptions(final List<Format> formats, final boolean remove) {
        if (remove) {
            this.formats = EnumSet.allOf(Format.class);
            this.formats.removeAll(formats);
            return;
        }

        this.formats = EnumSet.noneOf(Format.class);
        this.formats.addAll(formats);
    }

    public static FormatOptions builder() {
        return new FormatOptions();
    }

    public FormatOptions with(final Format... formats) {
        this.formats.clear();
        this.formats.addAll(Arrays.asList(formats));
        return this;
    }

    public FormatOptions without(final Format... formats) {
        this.formats.removeAll(Arrays.asList(formats));
        return this;
    }

    public FormatOptions defaultColor(final MessageColor defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    MessageColor getDefaultColor() {
        return defaultColor;
    }

    Set<Format> getFormats() {
        return formats;
    }

}
