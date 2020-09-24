package me.mattstudios.mfmsg.base.serializer.scanner;

import com.google.gson.JsonArray;
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
import me.mattstudios.mfmsg.base.serializer.Appender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ScanUtils {

    public static void scan(@NotNull final List<MessageNode> nodeList, @NotNull final Appender<?> appender) {
        appender.append("");

        final NodeScanner nodeScanner = new NodeScanner(nodeList);

        while (nodeScanner.hasNext()) {
            nodeScanner.next();

            final MessageNode messageNode = nodeScanner.peek();

            if (renderSpecial(messageNode, appender)) continue;

            final TextNode node = (TextNode) messageNode;

            final MessageColor color = node.getColor();

            if (color instanceof Gradient) {
                final List<MessageNode> gradientNodes = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientNodes.add(node);

                getColoredNodes(nodeScanner, gradientNodes, color);

                fancify(appender, gradientNodes, gradient);
                continue;
            }

            if (color instanceof Rainbow) {
                final List<MessageNode> rainbowNodes = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowNodes.add(node);

                getColoredNodes(nodeScanner, rainbowNodes, color);

                fancify(appender, rainbowNodes, rainbow);
                continue;
            }

            appender.appendNode(
                    node.getText(),
                    ((FlatColor) color).getColor(),
                    node.isBold(),
                    node.isItalic(),
                    node.isStrike(),
                    node.isUnderlined(),
                    node.isObfuscated(),
                    node.getActions()
            );

        }
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

    @NotNull
    private static JsonArray fancify(@NotNull Appender<?> appender, @NotNull final List<MessageNode> parts, @NotNull final MessageColor color) {
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
            if (renderSpecial(node, appender)) continue;

            final TextNode textNode = (TextNode) node;
            for (char character : textNode.getText().toCharArray()) {
                appender.appendNode(
                        String.valueOf(character),
                        fancy.next(),
                        textNode.isBold(),
                        textNode.isItalic(),
                        textNode.isStrike(),
                        textNode.isUnderlined(),
                        textNode.isObfuscated(),
                        textNode.getActions()
                );
            }
        }

        return jsonArray;
    }

    private static boolean renderSpecial(@NotNull final MessageNode node, @NotNull final Appender<?> appender) {
        if (node instanceof LineBreakNode) {
            appender.append("\n");
            return true;
        }

        if (node instanceof ReplaceableNode) {
            final ReplaceableNode replaceableNode = (ReplaceableNode) node;

            for (final TextNode textNode : replaceableNode.getNodes()) {
                appender.appendNode(
                        textNode.getText(),
                        null,
                        textNode.isBold(),
                        textNode.isItalic(),
                        textNode.isStrike(),
                        textNode.isUnderlined(),
                        textNode.isObfuscated(),
                        null
                );
            }

            return true;
        }

        return false;
    }

}
