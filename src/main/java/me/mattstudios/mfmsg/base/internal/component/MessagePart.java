package me.mattstudios.mfmsg.base.internal.component;

import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;

import java.util.List;
import java.util.stream.Collectors;

public final class MessagePart {

    private final String text;

    private final MessageColor color;

    private final boolean bold;
    private final boolean italic;
    private final boolean strike;
    private final boolean underline;
    private final boolean obfuscated;

    private final List<Action> actions;

    public MessagePart(String text, final MessageColor color, boolean bold, boolean italic, boolean strike, boolean underline, boolean obfuscated, final List<Action> actions) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.strike = strike;
        this.underline = underline;
        this.obfuscated = obfuscated;
        this.actions = actions;
    }

    public String getText() {
        return text;
    }

    public MessageColor getColor() {
        return color;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isStrike() {
        return strike;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isObfuscated() {
        return obfuscated;
    }

    public List<Action> getActions() {
        return actions;
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
        stringBuilder.append("] - Actions: ");
        if (actions == null || actions.isEmpty()) stringBuilder.append(" none");
        else stringBuilder.append(actions.stream().map(Action::test).collect(Collectors.joining(", ")));

        return stringBuilder.toString();
    }

}
