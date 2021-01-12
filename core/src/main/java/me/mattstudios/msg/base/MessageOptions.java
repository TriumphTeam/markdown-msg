package me.mattstudios.msg.base;

import me.mattstudios.msg.base.internal.Format;
import me.mattstudios.msg.base.internal.color.MessageColor;
import me.mattstudios.msg.commonmark.parser.ParserExtension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Options to enable and disable for the formats
 * TODO JavaDocs
 */
public final class MessageOptions {

    @NotNull
    private final Set<Format> formats;

    @NotNull
    private FormatData formatData = new FormatData();

    @NotNull
    private final List<ParserExtension> extensions;

    private MessageOptions(
            @NotNull final Set<Format> formats,
            @NotNull final FormatData formatData,
            @NotNull final List<ParserExtension> extensions
    ) {
        this.formats = formats;
        this.formatData = formatData;
        this.extensions = extensions;
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
    public FormatData getDefaultFormatData() {
        return formatData;
    }

    @NotNull
    public List<ParserExtension> getExtensions() {
        return extensions;
    }

    public static final class Builder {

        @NotNull
        private final Set<Format> formats;

        @NotNull
        private FormatData formatData = new FormatData();

        @NotNull
        private final List<ParserExtension> extensions = new ArrayList<>();

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
            formatData.setColor(color);
            return this;
        }

        public Builder setDefaultFormatData(@NotNull FormatData formatData) {
            formatData = formatData;
            return this;
        }

        public Builder extensions(@NotNull final Collection<ParserExtension> extensions) {
            this.extensions.addAll(extensions);
            return this;
        }

        public Builder extensions(@NotNull final ParserExtension... extensions) {
            this.extensions.addAll(Arrays.asList(extensions));
            return this;
        }

        public MessageOptions build() {
            return new MessageOptions(formats, formatData, extensions);
        }

    }

}
