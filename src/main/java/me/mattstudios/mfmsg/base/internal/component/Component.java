package me.mattstudios.mfmsg.base.internal.component;

public final class Component {

    private final String text;
    // Temp
    private final String color;

    private final boolean bold;
    private final boolean italic;
    private final boolean strike;
    private final boolean underline;
    private final boolean obfuscated;

    public Component(String text, final String color, boolean bold, boolean italic, boolean strike, boolean underline, boolean obfuscated) {
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
        stringBuilder.append("[").append(text).append(" - ");
        if (bold) stringBuilder.append("bold");
        if (italic) stringBuilder.append("italic");
        if (strike) stringBuilder.append("strike");
        if (underline) stringBuilder.append("underline");
        if (obfuscated) stringBuilder.append("obfuscated");
        stringBuilder.append(" - ").append(color).append("]");

        return stringBuilder.toString();
    }

}
