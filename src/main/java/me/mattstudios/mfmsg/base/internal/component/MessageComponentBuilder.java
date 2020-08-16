package me.mattstudios.mfmsg.base.internal.component;

public final class MessageComponentBuilder {

    private final String text;
    // Temp
    private String color;

    private boolean bold;
    private boolean italic;
    private boolean strike;
    private boolean underline;
    private boolean obfuscated;

    private MessageComponentBuilder(final String text) {
        this.text = text;
    }

    public static MessageComponentBuilder from(final String text) {
        return new MessageComponentBuilder(text);
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public void setBold(final boolean bold) {
        this.bold = bold;
    }

    public void setItalic(final boolean italic) {
        this.italic = italic;
    }

    public void setStrike(final boolean strike) {
        this.strike = strike;
    }

    public void setUnderline(final boolean underline) {
        this.underline = underline;
    }

    public void setObfuscated(final boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    public Component build() {
        return new Component(text, color, bold, italic, strike, underline, obfuscated);
    }

}
