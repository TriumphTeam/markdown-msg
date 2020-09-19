package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Stores all the information about each message node
 */
public final class MessageNode {

    @NotNull
    private final String text;

    @NotNull
    private final MessageColor color;

    private final boolean bold;
    private final boolean italic;
    private final boolean strike;
    private final boolean underlined;
    private final boolean obfuscated;

    @NotNull
    private final List<Action> actions;

    /**
     * Main part constructor with all it's attributes
     *
     * @param text       The text literal
     * @param color      The color of the part
     * @param bold       Whether or not the text is to be bold
     * @param italic     Whether or not the text is to be italic
     * @param strike     Whether or not the text is to be strikethrough
     * @param underlined Whether or not the text is to be underline
     * @param obfuscated Whether or not the text is to be obfuscated
     * @param actions    The list with all the actions
     */
    public MessageNode(@NotNull String text, @NotNull final MessageColor color, boolean bold, boolean italic, boolean strike, boolean underlined, boolean obfuscated, @NotNull final List<Action> actions) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.strike = strike;
        this.underlined = underlined;
        this.obfuscated = obfuscated;
        this.actions = actions;
    }

    /**
     * Gets the text literal
     *
     * @return The text
     */
    @NotNull
    public String getText() {
        return text;
    }

    /**
     * Gets the message color
     *
     * @return The message color
     */
    @NotNull
    public MessageColor getColor() {
        return color;
    }

    /**
     * Checks if the part is bold or not
     *
     * @return Whether or not it's bold
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Checks if the part is italic or not
     *
     * @return Whether or not it's italic
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * Checks if the part is strikethrough or not
     *
     * @return Whether or not it's strikethrough
     */
    public boolean isStrike() {
        return strike;
    }

    /**
     * Checks if the part is underlined or not
     *
     * @return Whether or not it's underlined
     */
    public boolean isUnderlined() {
        return underlined;
    }

    /**
     * Checks if the part is bold or not
     *
     * @return Whether or not it's bold
     */
    public boolean isObfuscated() {
        return obfuscated;
    }

    /**
     * Gets the actions the message has
     *
     * @return The message actions
     */
    @NotNull
    public List<Action> getActions() {
        return actions;
    }

}
