package me.mattstudios.mfmsg.base.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.mattstudios.mfmsg.base.bukkit.nms.ServerVersion;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.action.ClickAction;
import me.mattstudios.mfmsg.base.internal.action.HoverAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.color.handler.GradientHandler;
import me.mattstudios.mfmsg.base.internal.color.handler.RainbowHandler;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
import me.mattstudios.mfmsg.base.internal.component.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Will comment this class later, it's currently pretty messy
 */
public final class JsonSerializer {

    @NotNull
    private static final Gson gson = new Gson();

    private JsonSerializer() {}

    @NotNull
    public static String toString(@NotNull final List<MessageLine> lines) {
        return gson.toJson(toJson(lines));
    }

    @NotNull
    public static JsonArray toJson(@NotNull final List<MessageLine> lines) {
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

    @NotNull
    private static JsonArray convertLine(@NotNull final List<MessageNode> parts) {
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add("");
        for (int i = 0; i < parts.size(); i++) {
            final MessageNode part = parts.get(i);

            final MessageColor color = part.getColor();

            if (color instanceof Gradient) {
                final List<MessageNode> gradientParts = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessageNode newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    gradientParts.add(newPart);
                    i++;
                }

                jsonArray.addAll(toGradient(gradientParts, gradient));
                continue;
            }

            if (color instanceof Rainbow) {
                final List<MessageNode> rainbowParts = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowParts.add(part);

                while (i + 1 < parts.size()) {
                    final MessageNode newPart = parts.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    rainbowParts.add(newPart);
                    i++;
                }

                jsonArray.addAll(toRainbow(rainbowParts, rainbow));
                continue;
            }

            String colorString = null;
            if (color != null) colorString = ((FlatColor) color).getColor();

            jsonArray.add(serializePart(part.getText(), colorString, part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated(), part.getActions()));
        }

        return jsonArray;
    }

    @NotNull
    public static JsonObject serializePart(@NotNull final String text, @NotNull final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @NotNull final List<Action> actions) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);

        if (bold) jsonObject.addProperty("bold", true);
        if (italic) jsonObject.addProperty("italic", true);
        if (strike) jsonObject.addProperty("strikethrough", true);
        if (underline) jsonObject.addProperty("underlined", true);
        if (obfuscated) jsonObject.addProperty("obfuscated", true);

        jsonObject.addProperty("color", color);

        if (actions.isEmpty()) return jsonObject;

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

    @NotNull
    private static JsonArray toGradient(@NotNull final List<MessageNode> parts, @NotNull final Gradient gradient) {
        final JsonArray jsonArray = new JsonArray();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final GradientHandler gradientHandler = new GradientHandler(colors, length);

        for (final MessageNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                jsonArray.add(serializePart(String.valueOf(character), gradientHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated(), part.getActions()));
            }
        }

        return jsonArray;
    }

    @NotNull
    private static JsonArray toRainbow(@NotNull final List<MessageNode> parts, @NotNull final Rainbow rainbow) {
        final JsonArray jsonArray = new JsonArray();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();

        final RainbowHandler rainbowHandler = new RainbowHandler(length, rainbow.getSaturation(), rainbow.getBrightness());

        for (final MessageNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                jsonArray.add(serializePart(String.valueOf(character), rainbowHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated(), part.getActions()));
            }
        }

        return jsonArray;
    }

    @NotNull
    private static JsonObject getClickEvent(@NotNull final ClickAction clickAction, @NotNull final String type) {
        final JsonObject clickObject = new JsonObject();
        clickObject.addProperty("action", type);
        clickObject.addProperty("value", clickAction.getAction());
        return clickObject;
    }

}
