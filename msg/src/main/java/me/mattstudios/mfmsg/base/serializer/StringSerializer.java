package me.mattstudios.mfmsg.base.serializer;

import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.Gradient;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.color.Rainbow;
import me.mattstudios.mfmsg.base.internal.color.handlers.ColorMapping;
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
import java.util.regex.Pattern;

/**
 * This class is horrible, I can't think of anyway right now to abstract the JSON and String together
 * Will probably figure it later
 */
public final class StringSerializer {

    private static final Pattern CHARACTER = Pattern.compile(".");

    private StringSerializer() {}

    @NotNull
    public static String toString(@NotNull final List<MessageNode> nodeList) {
        final StringBuilder stringBuilder = new StringBuilder();

        final NodeScanner nodeScanner = new NodeScanner(nodeList);

        while (nodeScanner.hasNext()) {
            nodeScanner.next();

            final MessageNode messageNode = nodeScanner.peek();

            if (renderSpecial(messageNode, stringBuilder)) continue;

            final TextNode node = (TextNode) messageNode;

            final MessageColor color = node.getColor();

            if (color instanceof Gradient) {
                final List<MessageNode> gradientNodes = new ArrayList<>();
                final Gradient gradient = (Gradient) color;
                gradientNodes.add(node);

                getColoredNodes(nodeScanner, gradientNodes, color);

                stringBuilder.append(fancify(gradientNodes, gradient));
                continue;
            }

            if (color instanceof Rainbow) {
                final List<MessageNode> rainbowNodes = new ArrayList<>();
                final Rainbow rainbow = (Rainbow) color;
                rainbowNodes.add(node);

                getColoredNodes(nodeScanner, rainbowNodes, color);

                stringBuilder.append(fancify(rainbowNodes, rainbow));
                continue;
            }

            stringBuilder.append(
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

        return stringBuilder.toString();
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

    private static String renderNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (color != null) {
            if (color.startsWith("#")) {
                stringBuilder.append("§x").append(CHARACTER.matcher(color.substring(1)).replaceAll("§$0"));
            } else {
                stringBuilder.append("§").append(ColorMapping.fromName(color));
            }
        }

        if (bold) stringBuilder.append("§l");
        if (italic) stringBuilder.append("§o");
        if (strike) stringBuilder.append("§m");
        if (underline) stringBuilder.append("§n");
        if (obfuscated) stringBuilder.append("§k");

        stringBuilder.append(text);
        stringBuilder.append("§r");

        return stringBuilder.toString();
    }

    @NotNull
    private static StringBuilder fancify(@NotNull final List<MessageNode> parts, @NotNull final MessageColor color) {
        final StringBuilder stringBuilder = new StringBuilder();

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
            return stringBuilder;
        }

        for (final MessageNode node : parts) {
            if (renderSpecial(node, stringBuilder)) continue;

            final TextNode textNode = (TextNode) node;
            for (char character : textNode.getText().toCharArray()) {
                stringBuilder.append(
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

        return stringBuilder;
    }

    private static boolean renderSpecial(@NotNull final MessageNode node, @NotNull final StringBuilder stringBuilder) {
        if (node instanceof LineBreakNode) {
            stringBuilder.append("\n");
            return true;
        }

        if (node instanceof ReplaceableNode) {
            final ReplaceableNode replaceableNode = (ReplaceableNode) node;

            for (final TextNode textNode : replaceableNode.getNodes()) {
                stringBuilder.append(
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

    /*@NotNull
    public static String serializePart(@NotNull final String text, @NotNull final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (color.startsWith("#")) {
            stringBuilder.append("§x").append(RegexUtils.CHARACTER.matcher(color.substring(1)).replaceAll("§$0"));
        } else {
            stringBuilder.append("§").append(ColorMapping.fromName(color));
        }

        if (bold) stringBuilder.append("§l");
        if (italic) stringBuilder.append("§o");
        if (strike) stringBuilder.append("§m");
        if (underline) stringBuilder.append("§n");
        if (obfuscated) stringBuilder.append("§k");

        stringBuilder.append(text);
        stringBuilder.append("§r");

        return stringBuilder.toString();
    }

    @NotNull
    public static String toGradient(@NotNull final List<MessageNode> parts, @NotNull final Gradient gradient) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();
        final List<Color> colors = gradient.getColors();

        final GradientHandler gradientHandler = new GradientHandler(colors, length);

        for (final MessageNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(serializePart(String.valueOf(character), gradientHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
            }
        }

        return stringBuilder.toString();
    }

    @NotNull
    private static String toRainbow(@NotNull final List<MessageNode> parts, @NotNull final Rainbow rainbow) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = parts.stream().mapToInt(part -> part.getText().length()).sum();

        final RainbowHandler rainbowHandler = new RainbowHandler(length, rainbow.getSaturation(), rainbow.getBrightness());

        for (final MessageNode part : parts) {
            for (char character : part.getText().toCharArray()) {
                stringBuilder.append(serializePart(String.valueOf(character), rainbowHandler.next(), part.isBold(), part.isItalic(), part.isStrike(), part.isUnderlined(), part.isObfuscated()));
            }
        }

        return stringBuilder.toString();
    }*/

}
