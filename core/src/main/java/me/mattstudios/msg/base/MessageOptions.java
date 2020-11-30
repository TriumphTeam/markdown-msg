package me.mattstudios.msg.base;

import me.mattstudios.msg.base.internal.Format;
import me.mattstudios.msg.base.internal.color.FlatColor;
import me.mattstudios.msg.base.internal.color.MessageColor;
import me.mattstudios.msg.base.internal.extensions.ReplaceableHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

/**
 * Options to enable and disable for the formats
 * TODO JavaDocs
 */
public final class MessageOptions {

    @NotNull
    private final Set<Format> formats;
    @NotNull
    private final MessageColor defaultColor;

    @Nullable
    private final ReplaceableHandler replaceableHandler;

    private MessageOptions(@NotNull final Set<Format> formats, @NotNull final MessageColor defaultColor, @Nullable final ReplaceableHandler replaceableHandler) {
        this.formats = formats;
        this.defaultColor = defaultColor;
        this.replaceableHandler = replaceableHandler;
    }

    public static Builder builder(@NotNull final Set<Format> formats) {
        return new Builder(formats);
    }

    public static Builder builder() {
        return new Builder(Format.ALL);
    }

    public boolean hasFormat(@NotNull final Format format) {
        return formats.contains(format);
    }

    @NotNull
    public Set<Format> getFormats() {
        return formats;
    }

    @NotNull
    public MessageColor getDefaultColor() {
        return defaultColor;
    }

    @Nullable
    public ReplaceableHandler getReplaceableHandler() {
        return replaceableHandler;
    }

    public static final class Builder {

        @NotNull
        private final Set<Format> formats;

        @NotNull
        private MessageColor defaultColor = new FlatColor("white");

        @Nullable
        private ReplaceableHandler replaceableHandler = null;

        public Builder(@NotNull final Set<Format> formats) {
            this.formats = formats;
        }

        public Builder addFormat(@NotNull final Format... format) {
            formats.addAll(Arrays.asList(format));
            return this;
        }

        public Builder removeFormat(@NotNull final Format... format) {
            formats.removeAll(Arrays.asList(format));
            return this;
        }

        public Builder setDefaultColor(@NotNull final MessageColor color) {
            defaultColor = color;
            return this;
        }

        public Builder setReplaceableHandler(@NotNull final ReplaceableHandler replaceableHandler) {
            this.replaceableHandler = replaceableHandler;
            return this;
        }

        public MessageOptions build() {
            return new MessageOptions(formats, defaultColor, replaceableHandler);
        }

    }

}
