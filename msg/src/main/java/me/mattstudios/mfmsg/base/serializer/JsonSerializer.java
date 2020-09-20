package me.mattstudios.mfmsg.base.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.mattstudios.mfmsg.base.bukkit.nms.ServerVersion;
import me.mattstudios.mfmsg.base.internal.action.ClickMessageAction;
import me.mattstudios.mfmsg.base.internal.action.HoverMessageAction;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.color.handler.GradientHandler;
import me.mattstudios.mfmsg.base.internal.color.handler.RainbowHandler;
import me.mattstudios.mfmsg.base.internal.component.BasicNode;
import me.mattstudios.mfmsg.base.internal.component.LineBreakNode;
import me.mattstudios.mfmsg.base.internal.component.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Will comment this class later, it's currently pretty messy
 * TODO THIS NEEDS A REWRITE
 */
public final class JsonSerializer {

    @NotNull
    private static final Gson GSON = new Gson();

    private JsonSerializer() {}

    @NotNull
    public static String toString(@NotNull final List<MessageNode> nodes) {
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add("");

        for (int i = 0; i < nodes.size(); i++) {
            final MessageNode node = nodes.get(i);

            if (node instanceof LineBreakNode) {
                jsonArray.add("\n");
                continue;
            }

            final BasicNode basicNode = (BasicNode) node;

            final MessageColor color = basicNode.getColor();

            if (color instanceof Gradient) {
                final List<BasicNode> gradientParts = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientParts.add(basicNode);

                while (i + 1 < nodes.size()) {
                    if (nodes.get(i + 1) instanceof LineBreakNode) {
                        i++;
                        continue;
                    }

                    final BasicNode newPart = (BasicNode) nodes.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    gradientParts.add(newPart);
                    i++;
                }

                jsonArray.addAll(toGradient(gradientParts, gradient));
                continue;
            }

            if (color instanceof Rainbow) {
                final List<BasicNode> rainbowParts = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowParts.add(basicNode);

                while (i + 1 < nodes.size()) {
                    System.out.println("Hello?");
                    if (nodes.get(i + 1) instanceof LineBreakNode) {
                        i++;
                        continue;
                    }
                    System.out.println("passed?");
                    final BasicNode newPart = (BasicNode) nodes.get(i + 1);
                    if (!color.equals(newPart.getColor())) break;

                    rainbowParts.add(newPart);
                    i++;
                }

                jsonArray.addAll(toRainbow(rainbowParts, rainbow));
                continue;
            }

            String colorString = null;
            if (color != null) colorString = ((FlatColor) color).getColor();

            jsonArray.add(serializePart(basicNode.getText(), colorString, basicNode.isBold(), basicNode.isItalic(), basicNode.isStrike(), basicNode.isUnderlined(), basicNode.isObfuscated(), basicNode.getActions()));
        }

        return  GSON.toJson(jsonArray);
    }

    @NotNull
    public static JsonObject serializePart(@NotNull final String text, @NotNull final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @NotNull final List<MessageAction> messageActions) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);

        if (bold) jsonObject.addProperty("bold", true);
        if (italic) jsonObject.addProperty("italic", true);
        if (strike) jsonObject.addProperty("strikethrough", true);
        if (underline) jsonObject.addProperty("underlined", true);
        if (obfuscated) jsonObject.addProperty("obfuscated", true);

        jsonObject.addProperty("color", color);

        if (messageActions == null || messageActions.isEmpty()) return jsonObject;

        for (final MessageAction messageAction : messageActions) {
            if (messageAction instanceof HoverMessageAction) {
                final JsonObject hoverObject = new JsonObject();
                hoverObject.addProperty("action", "show_text");
                if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
                    //hoverObject.add("value", toJson(((HoverMessageAction) messageAction).getLines()));
                } else {
                    //hoverObject.add("contents", toJson(((HoverMessageAction) messageAction).getLines()));
                }
                jsonObject.add("hoverEvent", hoverObject);
                continue;
            }

            final ClickMessageAction clickAction = (ClickMessageAction) messageAction;

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
    private static JsonArray toGradient(@NotNull final List<BasicNode> parts, @NotNull final Gradient gradient) {
        final JsonArray jsonArray = new JsonArray();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final GradientHandler gradientHandler = new GradientHandler(colors, length);

        for (final BasicNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                jsonArray.add(serializePart(String.valueOf(character), gradientHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated(), part.getActions()));
            }
        }

        return jsonArray;
    }

    @NotNull
    private static JsonArray toRainbow(@NotNull final List<BasicNode> parts, @NotNull final Rainbow rainbow) {
        final JsonArray jsonArray = new JsonArray();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();

        final RainbowHandler rainbowHandler = new RainbowHandler(length, rainbow.getSaturation(), rainbow.getBrightness());

        for (final BasicNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                jsonArray.add(serializePart(String.valueOf(character), rainbowHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated(), part.getActions()));
            }
        }

        return jsonArray;
    }

    @NotNull
    private static JsonObject getClickEvent(@NotNull final ClickMessageAction clickAction, @NotNull final String type) {
        final JsonObject clickObject = new JsonObject();
        clickObject.addProperty("action", type);
        clickObject.addProperty("value", clickAction.getAction());
        return clickObject;
    }

}
