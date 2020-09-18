package me.mattstudios.mfmsg.base.internal.token;

import org.jetbrains.annotations.NotNull;

/**
 * Base text token to be parsed later
 */
public final class TextToken implements Token {

    @NotNull
    private final String text;

    /**
     * Main constructor
     *
     * @param text Text to hold and be parsed later
     */
    public TextToken(@NotNull final String text) {
        this.text = text;
    }

    /**
     * Gets the text to be used
     *
     * @return The main text
     */
    @NotNull
    public String getText() {
        return text;
    }

}
