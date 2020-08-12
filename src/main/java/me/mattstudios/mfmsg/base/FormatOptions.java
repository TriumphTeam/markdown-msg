package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class FormatOptions {

    private final Set<Format> formats;

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

    public static FormatOptions with(final Format... formats) {
        return new FormatOptions(Arrays.asList(formats), false);
    }

    public static FormatOptions without(final Format... formats) {
        return new FormatOptions(Arrays.asList(formats), true);
    }

    Set<Format> getFormats() {
        return formats;
    }

}
