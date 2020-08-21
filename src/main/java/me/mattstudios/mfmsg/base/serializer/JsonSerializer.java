package me.mattstudios.mfmsg.base.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.action.ClickAction;
import me.mattstudios.mfmsg.base.internal.action.HoverAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.handler.GradientHandler;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.nms.ServerVersion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class JsonSerializer {

    private static final Gson gson = new Gson();

    private JsonSerializer() {}

    public static String toString(final List<MessageLine> lines) {
        return gson.toJson(toJson(lines));
    }

    public static JsonArray toJson(final List<MessageLine> lines) {
        final JsonArray jsonArray = new JsonArray();

        final Iterator<MessageLine> iterator = lines.iterator();
        while (iterator.hasNext()) {
            jsonArray.add(convertLine(iterator.next().getParts()));
            if (iterator.hasNext()) {
                final JsonObject newLine = new JsonObject();
                newLine.addProperty("text", "\n");
                jsonArray.add(newLine);
            }
        }

        return jsonArray;
    }

    private static JsonArray convertLine(final List<MessagePart> parts) {
        final JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < parts.size(); i++) {
            final MessagePart part = parts.get(i);

            final MessageColor color = part.getColor();

            if (color instanceof Gradient) {
                final List<MessagePart> gradientParts = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessagePart newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    gradientParts.add(newPart);
                    i++;
                }

                jsonArray.addAll(GradientHandler.toGradient(gradientParts, gradient));
                continue;
            }

            String colorString = null;
            if (color != null) colorString = ((FlatColor) color).getColor();

            jsonArray.add(toObject(part.getText(), colorString, part.isBold(), part.isItalic(), part.isStrike(), part.isUnderline(), part.isObfuscated(), part.getActions()));
        }

        return jsonArray;
    }

    public static JsonObject toObject(final String text, final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, final List<Action> actions) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);

        jsonObject.addProperty("bold", bold);
        jsonObject.addProperty("italic", italic);
        jsonObject.addProperty("strikethrough", strike);
        jsonObject.addProperty("underlined", underline);
        jsonObject.addProperty("obfuscated", obfuscated);

        if (color != null) jsonObject.addProperty("color", color);

        if (actions == null || actions.isEmpty()) return jsonObject;

        for (final Action action : actions) {
            if (action instanceof HoverAction) {
                final JsonObject hoverObject = new JsonObject();
                hoverObject.addProperty("action", "show_text");
                if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
                    hoverObject.add("value", toJson(((HoverAction) action).getLines()));
                } else {
                    hoverObject.add("contents", toJson(((HoverAction) action).getLines()));
                }
                jsonObject.add("hoverEvent", hoverObject);
                continue;
            }

            final ClickAction clickAction = (ClickAction) action;

            switch (clickAction.getActionType()) {
                case ACTION_COMMAND:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "run_command"));
                    continue;

                case ACTION_SUGGEST:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "suggest_command"));
                    continue;

                case ACTION_URL:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "open_url"));
                    continue;

                case ACTION_CLIPBOARD:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "copy_to_clipboard"));
            }
        }

        return jsonObject;
    }

    private static JsonObject getClickEvent(final ClickAction clickAction, final String type) {
        final JsonObject clickObject = new JsonObject();
        clickObject.addProperty("action", type);
        clickObject.addProperty("value", clickAction.getAction());
        return clickObject;
    }

}
