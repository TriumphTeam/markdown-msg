package me.mattstudios.mfmsg.base.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.mattstudios.mfmsg.base.internal.util.Version;
import me.mattstudios.mfmsg.base.internal.action.ClickMessageAction;
import me.mattstudios.mfmsg.base.internal.action.HoverMessageAction;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.color.handlers.Fancy;
import me.mattstudios.mfmsg.base.internal.color.handlers.GradientHandler;
import me.mattstudios.mfmsg.base.internal.color.handlers.RainbowHandler;
import me.mattstudios.mfmsg.base.internal.components.LineBreakNode;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.components.ReplaceableNode;
import me.mattstudios.mfmsg.base.internal.components.TextNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static String toString(@NotNull final List<MessageNode> nodeList) {
        return GSON.toJson(toJson(nodeList));
    }


    @NotNull
    public static JsonArray toJson(@NotNull final List<MessageNode> nodeList) {
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add("");

        final NodeScanner nodeScanner = new NodeScanner(nodeList);

        while (nodeScanner.hasNext()) {
            nodeScanner.next();

            final MessageNode messageNode = nodeScanner.peek();

            if (renderSpecial(messageNode, jsonArray)) continue;

            final TextNode node = (TextNode) messageNode;

            final MessageColor color = node.getColor();

            if (color instanceof Gradient) {
                final List<MessageNode> gradientNodes = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientNodes.add(node);

                getColoredNodes(nodeScanner, gradientNodes, color);

                jsonArray.addAll(fancify(gradientNodes, gradient));
                continue;
            }

            if (color instanceof Rainbow) {
                final List<MessageNode> rainbowNodes = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowNodes.add(node);

                getColoredNodes(nodeScanner, rainbowNodes, color);

                jsonArray.addAll(fancify(rainbowNodes, rainbow));
                continue;
            }

            jsonArray.add(
                    renderNode(
                            node.getText(),
                            ((FlatColor) color).getColor(),
                            node.isBold(),
                            node.isItalic(),
                            node.isStrike(),
                            node.isUnderlined(),
                            node.isObfuscated(),
                            node.getActions()
                    )
            );

        }

        return jsonArray;
    }

    private static void getColoredNodes(final NodeScanner nodeScanner, final List<MessageNode> nodes, final MessageColor color) {
        while (nodeScanner.hasNext()) {
            nodeScanner.next();

            final MessageNode nextNode = nodeScanner.peek();

            if (nextNode instanceof LineBreakNode || nextNode instanceof ReplaceableNode) {
                nodes.add(nextNode);
                continue;
            }

            final TextNode textNode = (TextNode) nextNode;
            if (!color.equals(textNode.getColor())) {
                nodeScanner.previous();
                break;
            }

            nodes.add(textNode);
        }
    }

    private static JsonObject renderNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);

        if (bold) jsonObject.addProperty("bold", true);
        if (italic) jsonObject.addProperty("italic", true);
        if (strike) jsonObject.addProperty("strikethrough", true);
        if (underline) jsonObject.addProperty("underlined", true);
        if (obfuscated) jsonObject.addProperty("obfuscated", true);

        if (color != null) jsonObject.addProperty("color", color);

        if (actions == null || actions.isEmpty()) return jsonObject;

        for (final MessageAction messageAction : actions) {
            if (messageAction instanceof HoverMessageAction) {
                final JsonObject hoverObject = new JsonObject();
                hoverObject.addProperty("action", "show_text");
                if (Version.CURRENT_VERSION.isColorLegacy()) {
                    hoverObject.add("value", toJson(((HoverMessageAction) messageAction).getNodes()));
                } else {
                    hoverObject.add("contents", toJson(((HoverMessageAction) messageAction).getNodes()));
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
    private static JsonArray fancify(@NotNull final List<MessageNode> parts, @NotNull final MessageColor color) {
        final JsonArray jsonArray = new JsonArray();

        final int length = parts.stream()
                .filter(TextNode.class::isInstance)
                .mapToInt(part -> ((TextNode) part).getText().length())
                .sum();

        final Fancy fancy;
        if (color instanceof Rainbow) {
            final Rainbow rainbow = (Rainbow) color;
            fancy = new RainbowHandler(length, rainbow.getSaturation(), rainbow.getBrightness());
        } else if (color instanceof Gradient) {
            final Gradient gradient = (Gradient) color;
            fancy = new GradientHandler(gradient.getColors(), length);
        } else {
            return jsonArray;
        }

        for (final MessageNode node : parts) {
            if (renderSpecial(node, jsonArray)) continue;

            final TextNode textNode = (TextNode) node;
            for (char character : textNode.getText().toCharArray()) {
                jsonArray.add(
                        renderNode(
                                String.valueOf(character),
                                fancy.next(),
                                textNode.isBold(),
                                textNode.isItalic(),
                                textNode.isStrike(),
                                textNode.isUnderlined(),
                                textNode.isObfuscated(),
                                textNode.getActions()
                        )
                );
            }
        }

        return jsonArray;
    }

    private static boolean renderSpecial(@NotNull final MessageNode node, @NotNull final JsonArray jsonArray) {
        if (node instanceof LineBreakNode) {
            jsonArray.add("\n");
            return true;
        }

        if (node instanceof ReplaceableNode) {
           final ReplaceableNode replaceableNode = (ReplaceableNode) node;

           for (final TextNode textNode : replaceableNode.getNodes()) {
               jsonArray.add(
                       renderNode(
                               textNode.getText(),
                               null,
                               textNode.isBold(),
                               textNode.isItalic(),
                               textNode.isStrike(),
                               textNode.isUnderlined(),
                               textNode.isObfuscated(),
                               null)
               );
           }

            return true;
        }

        return false;
    }

    @NotNull
    private static JsonObject getClickEvent(@NotNull final ClickMessageAction clickAction, @NotNull final String type) {
        final JsonObject clickObject = new JsonObject();
        clickObject.addProperty("action", type);
        clickObject.addProperty("value", clickAction.getAction());
        return clickObject;
    }

}
