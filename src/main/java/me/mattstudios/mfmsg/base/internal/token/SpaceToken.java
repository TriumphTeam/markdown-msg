package me.mattstudios.mfmsg.base.internal.token;

import org.jetbrains.annotations.NotNull;

/**
 * This token is needed due to how common mark works and how it trims all the messages passing
 */
public final class SpaceToken implements Token {

    @NotNull
    private final String text;

    /**
     * The main constructor
     *
     * @param text The space character
     */
    public SpaceToken(@NotNull final String text) {
        this.text = text;
    }

    /**
     * Gets the space text
     *
     * @return The space text
     */
    @NotNull
    public String getText() {
        return text;
    }

}
