package me.mattstudios.mfmsg.base.internal.component;

import com.google.gson.JsonObject;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;

import java.util.List;

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

    public JsonObject test() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);
        if (color != null) jsonObject.addProperty("color", ((FlatColor) color).getColor());
        jsonObject.addProperty("bold", bold);
        jsonObject.addProperty("italic", italic);
        jsonObject.addProperty("strikethrough", strike);
        jsonObject.addProperty("underline", underline);
        jsonObject.addProperty("obfuscated", obfuscated);
        return jsonObject;
    }

}
