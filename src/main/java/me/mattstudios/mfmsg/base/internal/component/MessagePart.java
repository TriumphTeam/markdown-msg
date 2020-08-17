package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.color.MessageColor;

public final class MessagePart {

    private final String text;

    private final MessageColor color;

    private final boolean bold;
    private final boolean italic;
    private final boolean strike;
    private final boolean underline;
    private final boolean obfuscated;

    public MessagePart(String text, final MessageColor color, boolean bold, boolean italic, boolean strike, boolean underline, boolean obfuscated) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.strike = strike;
        this.underline = underline;
        this.obfuscated = obfuscated;
    }

    public String test() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("[{").append(text).append("} - ")
                .append("bold: ").append(bold).append(", ")
                .append("italic: ").append(italic).append(", ")
                .append("strike: ").append(strike).append(", ")
                .append("underline: ").append(underline).append(", ")
                .append("obfuscated: ").append(obfuscated).append(", ")
                .append( "color: ");
        if (color == null) stringBuilder.append("null");
        else stringBuilder.append(color.test());
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

}
