package me.mattstudios.msg.base.serializer;

import me.mattstudios.msg.base.internal.color.FlatColor;
import me.mattstudios.msg.base.internal.color.GradientColor;
import me.mattstudios.msg.base.internal.color.MessageColor;
import me.mattstudios.msg.base.internal.color.RainbowColor;
import me.mattstudios.msg.base.internal.color.handlers.Fancy;
import me.mattstudios.msg.base.internal.color.handlers.GradientHandler;
import me.mattstudios.msg.base.internal.color.handlers.RainbowHandler;
import me.mattstudios.msg.base.internal.nodes.MessageNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NodeScanner {

    @NotNull
    private final List<MessageNode> nodes;
    private int index;

    // Current node
    private MessageNode node = null;

    /**
     * Constructor of the node Scanner
     *
     * @param nodes The list of nodes to scan over
     */
    public NodeScanner(@NotNull final List<MessageNode> nodes) {
        this.nodes = nodes;
        this.index = -1;
    }

    /**
     * Peeks the current node
     *
     * @return The current node
     */
    public MessageNode peek() {
        return node;
    }

    /**
     * Checks if has next node or not
     *
     * @return False if no longer has nodes
     */
    public boolean hasNext() {
        return index < nodes.size() - 1;
    }

    /**
     * Goes to the next node
     */
    public void next() {
        index++;
        setNode(nodes.get(index));
    }

    /**
     * Steps back to previous node
     */
    public void previous() {
        if (index > 0) index--;
        setNode(nodes.get(index));
    }

    /**
     * Sets the current node
     *
     * @param node The current node from the list
     */
    private void setNode(@NotNull MessageNode node) {
        this.node = node;
    }

    /**
     * Util method to scan over the node and handle it
     *
     * @param nodeList The node list
     * @param appender The appender to append each node
     */
    public static void scan(@NotNull final List<MessageNode> nodeList, @NotNull final Appender<?> appender) {
        appender.append("");

        final NodeScanner nodeScanner = new NodeScanner(nodeList);

        while (nodeScanner.hasNext()) {
            nodeScanner.next();

            final MessageNode node = nodeScanner.peek();
            final MessageColor color = node.getColor();

            if (color instanceof GradientColor) {
                final List<MessageNode> gradientNodes = new ArrayList<>();
                final GradientColor gradientColor = (GradientColor) color;
                gradientNodes.add(node);

                getColoredNodes(nodeScanner, gradientNodes, color);

                fancify(appender, gradientNodes, gradientColor);
                continue;
            }

            if (color instanceof RainbowColor) {
                final List<MessageNode> rainbowNodes = new ArrayList<>();
                final RainbowColor rainbowColor = (RainbowColor) color;
                rainbowNodes.add(node);

                getColoredNodes(nodeScanner, rainbowNodes, color);

                fancify(appender, rainbowNodes, rainbowColor);
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

            if (!color.equals(nextNode.getColor())) {
                nodeScanner.previous();
                break;
            }

            nodes.add(nextNode);
        }
    }

    private static void fancify(@NotNull Appender<?> appender, @NotNull final List<MessageNode> parts, @NotNull final MessageColor color) {
        final int length = getFancyLength(parts);

        final Fancy fancy;
        if (color instanceof RainbowColor) {
            final RainbowColor rainbowColor = (RainbowColor) color;
            fancy = new RainbowHandler(length, rainbowColor.getSaturation(), rainbowColor.getBrightness());
        } else if (color instanceof GradientColor) {
            final GradientColor gradientColor = (GradientColor) color;
            fancy = new GradientHandler(gradientColor.getColors(), length);
        } else {
            return;
        }

        for (final MessageNode node : parts) {
            for (char character : node.getText().toCharArray()) {
                appender.appendNode(
                        String.valueOf(character),
                        fancy.next(),
                        node.isBold(),
                        node.isItalic(),
                        node.isStrike(),
                        node.isUnderlined(),
                        node.isObfuscated(),
                        node.getActions()
                );
            }
        }
    }

    private static int getFancyLength(@NotNull final List<MessageNode> nodes) {
        int length = 0;
        for (final MessageNode node : nodes) {
            length += node.getText().length();
        }
        return length;
    }

}
