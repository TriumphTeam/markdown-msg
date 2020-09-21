package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import org.jetbrains.annotations.NotNull;

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
    private MessageColor defaultColor = new FlatColor("white");

    /**
     * Main constructor with all the formats
     */
    MessageOptions() {
        this.formats = EnumSet.allOf(Format.class);
    }

    /**
     * Gets the format options builder
     *
     * @return The format options builder
     */
    @NotNull
    public static MessageOptions builder() {
        return new MessageOptions();
    }

    /**
     * Creates a format options with only the give formats
     *
     * @param formats The formats to add
     * @return The format options object
     */
    @NotNull
    public MessageOptions with(@NotNull final Format... formats) {
        this.formats.clear();
        this.formats.addAll(Arrays.asList(formats));
        return this;
    }

    /**
     * Creates a format options without the give formats
     *
     * @param formats The formats to remove
     * @return The format options object
     */
    @NotNull
    public MessageOptions without(@NotNull final Format... formats) {
        this.formats.removeAll(Arrays.asList(formats));
        return this;
    }

    /**
     * Sets the default color to use in all messages
     *
     * @param defaultColor The default {@link MessageColor}
     * @return The format options object
     */
    @NotNull
    public MessageOptions defaultColor(@NotNull final MessageColor defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    /**
     * Gets the default color
     *
     * @return The default color
     */
    @NotNull
    MessageColor getDefaultColor() {
        return defaultColor;
    }

    /**
     * Gets the formats
     *
     * @return The list with the formats to use
     */
    @NotNull
    Set<Format> getFormats() {
        return formats;
    }

}
