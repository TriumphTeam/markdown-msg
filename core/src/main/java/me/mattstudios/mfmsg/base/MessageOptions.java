package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.extensions.ReplaceableHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Options to enable and disable for the formats
 */
public final class MessageOptions {

    @NotNull
    private final Set<Format> formats;
    @NotNull
    private final MessageColor defaultColor;

    @Nullable
    private final ReplaceableHandler replaceableHandler;

    /**
     * Main constructor with all the formats
     */
    private MessageOptions(@NotNull final Set<Format> formats, @NotNull final MessageColor defaultColor, @Nullable final ReplaceableHandler replaceableHandler) {
        this.formats = formats;
        this.defaultColor = defaultColor;
        this.replaceableHandler = replaceableHandler;
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

    public ReplaceableHandler getReplaceableHandler() {
        return replaceableHandler;
    }

    public static final class Builder {

        @NotNull
        private Set<Format> formats = EnumSet.noneOf(Format.class);
        @NotNull
        private MessageColor defaultColor = new FlatColor("white");

        @Nullable
        private ReplaceableHandler replaceableHandler = null;

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

        public Builder setReplaceableHandler(@NotNull ReplaceableHandler replaceableHandler) {
            this.replaceableHandler = replaceableHandler;
            return this;
        }

        public MessageOptions build() {
            return new MessageOptions(formats, defaultColor, replaceableHandler);
        }

    }

}
