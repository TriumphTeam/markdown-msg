package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.extensions.ObfuscatedExtension;
import me.mattstudios.mfmsg.base.internal.extensions.StrikethroughExtension;
import me.mattstudios.mfmsg.base.internal.extensions.UnderlineExtension;
import me.mattstudios.mfmsg.commonmark.Extension;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Options to enable and disable for the formats
 */
public final class MessageOptions {

    @NotNull
    private final Set<Format> formats;
    @NotNull
    private final MessageColor defaultColor;
    @NotNull
    private final Set<Extension> extensions;

    /**
     * Main constructor with all the formats
     */
    private MessageOptions(@NotNull final Set<Format> formats, @NotNull final MessageColor defaultColor, @NotNull final Set<Extension> extensions) {
        this.formats = formats;
        this.defaultColor = defaultColor;
        this.extensions = extensions;
    }

    public boolean hasFormat(@NotNull final Format format) {
        return formats.contains(format);
    }

    public Set<Format> getFormats() {
        return formats;
    }

    public MessageColor getDefaultColor() {
        return defaultColor;
    }

    public Set<Extension> getExtensions() {
        return extensions;
    }

    public static final class Builder {

        @NotNull
        private Set<Format> formats = EnumSet.noneOf(Format.class);
        @NotNull
        private MessageColor defaultColor = new FlatColor("white");

        @NotNull
        private final Set<Extension> extensions = new HashSet<>(
                Arrays.asList(
                        StrikethroughExtension.create(),
                        UnderlineExtension.create(),
                        ObfuscatedExtension.create()
                )
        );

        public Builder(@NotNull final Format... format) {
            formats.addAll(Arrays.asList(format));
        }

        public Builder(@NotNull final Set<Format> formats) {
            this.formats = formats;
        }

        public Builder addFormat(@NotNull Format... format) {
            formats.addAll(Arrays.asList(format));
            return this;
        }

        public Builder removeFormat(@NotNull Format... format) {
            formats.removeAll(Arrays.asList(format));
            return this;
        }

        public Builder setDefaultColor(@NotNull MessageColor color) {
            defaultColor = color;
            return this;
        }

        public Builder addExtension(@NotNull Extension... extension) {
            extensions.addAll(Arrays.asList(extension));
            return this;
        }

        public MessageOptions build() {
            return new MessageOptions(formats, defaultColor, extensions);
        }

    }

}
