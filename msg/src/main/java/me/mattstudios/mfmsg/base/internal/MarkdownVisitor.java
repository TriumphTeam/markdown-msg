package me.mattstudios.mfmsg.base.internal;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.components.LineBreakNode;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.components.ReplaceableNode;
import me.mattstudios.mfmsg.base.internal.components.TextNode;
import me.mattstudios.mfmsg.base.internal.extensions.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extensions.node.Replaceable;
import me.mattstudios.mfmsg.base.internal.extensions.node.Strikethrough;
import me.mattstudios.mfmsg.base.internal.extensions.node.Underline;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import me.mattstudios.mfmsg.commonmark.node.AbstractVisitor;
import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Emphasis;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.StrongEmphasis;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.node.mf.Action;
import me.mattstudios.mfmsg.commonmark.node.mf.Color;
import me.mattstudios.mfmsg.commonmark.node.mf.Gradient;
import me.mattstudios.mfmsg.commonmark.node.mf.LineBreak;
import me.mattstudios.mfmsg.commonmark.node.mf.Rainbow;
import me.mattstudios.mfmsg.commonmark.node.mf.Reset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public final class MarkdownVisitor extends AbstractVisitor {

    @NotNull
    private final MessageOptions messageOptions;
    @NotNull
    private List<MessageNode> nodes;

    // Text properties
    private boolean bold = false;
    private boolean italic = false;
    private boolean strike = false;
    private boolean underline = false;
    private boolean obfuscated = false;

    @Nullable
    private Replaceable replaceable = null;

    @NotNull
    private MessageColor currentColor;
    @Nullable
    private List<MessageAction> actions = null;

    public MarkdownVisitor(@NotNull final List<MessageNode> nodes, @NotNull final MessageOptions messageOptions) {
        this.nodes = nodes;
        this.messageOptions = messageOptions;
        this.currentColor = messageOptions.getDefaultColor();
    }

    /**
     * Visits the node's components
     *
     * @param node The node to visit
     *             //* @param appender The appender to append text to
     */
    public void visitComponents(final Node node) {
        node.accept(this);
    }

    /**
     * Handles italic
     *
     * @param emphasis The italic is matched by either `* *` or `_ _`
     */
    @Override
    public void visit(final Emphasis emphasis) {
        if (!messageOptions.hasFormat(Format.ITALIC)) {
            visitChildren(emphasis);
            return;
        }

        italic = true;
        visitChildren(emphasis);
        italic = false;
    }

    /**
     * Handles bold
     *
     * @param strongEmphasis Bold is matched by `** **`
     */
    @Override
    public void visit(final StrongEmphasis strongEmphasis) {
        if (!messageOptions.hasFormat(Format.BOLD)) {
            visitChildren(strongEmphasis);
            return;
        }

        bold = true;
        visitChildren(strongEmphasis);
        bold = false;
    }

    /**
     * Handles extended nodes
     *
     * @param customNode The {@link CustomNode} can be either {@link Underline}, {@link Strikethrough}, or {@link Obfuscated}
     */
    @Override
    public void visit(final CustomNode customNode) {
        if (customNode instanceof Underline) {
            if (!messageOptions.hasFormat(Format.UNDERLINE)) {
                visitChildren(customNode);
                return;
            }
            underline = true;
            visitChildren(customNode);
            underline = false;
            return;
        }

        if (customNode instanceof Obfuscated) {
            if (!messageOptions.hasFormat(Format.OBFUSCATED)) {
                visitChildren(customNode);
                return;
            }

            obfuscated = true;
            visitChildren(customNode);
            obfuscated = false;
            return;
        }

        if (customNode instanceof Strikethrough) {
            if (!messageOptions.hasFormat(Format.STRIKETHROUGH)) {
                visitChildren(customNode);
                return;
            }

            strike = true;
            visitChildren(customNode);
            strike = false;
        }

        if (customNode instanceof Replaceable) {
            replaceable = (Replaceable) customNode;
            visitChildren(customNode);
            replaceable = null;
        }

    }

    @Override
    public void visit(final Color color) {
        if (color.isLegacy()) currentColor = MessageColor.from(color.getColor().charAt(0));
        else currentColor = MessageColor.from(color.getColor());
        visitChildren(color);
    }

    @Override
    public void visit(final Reset reset) {
        currentColor = messageOptions.getDefaultColor();
        visitChildren(reset);
    }

    @Override
    public void visit(final Rainbow rainbow) {
        currentColor = MessageColor.from(rainbow.getSaturation(), rainbow.getBrightness());
        visitChildren(rainbow);
    }

    @Override
    public void visit(final Gradient gradient) {
        currentColor = MessageColor.from(gradient.getHexes());
        visitChildren(gradient);
    }

    @Override
    public void visit(final Action action) {
        final List<MessageAction> actions = new ArrayList<>();
        for (final Entry<String, String> entry : action.getActions().entrySet()) {
            switch (entry.getKey().toLowerCase()) {
                case "hover":
                    if (!messageOptions.hasFormat(Format.ACTION_HOVER)) break;
                    final MessageParser parser = new MessageParser(messageOptions);
                    parser.parse(entry.getValue());
                    actions.add(MessageAction.from(parser.build()));
                    break;

                case "command":
                    if (!messageOptions.hasFormat(Format.ACTION_COMMAND)) break;
                    actions.add(MessageAction.from(Format.ACTION_COMMAND, entry.getValue()));
                    break;

                case "suggest":
                    if (!messageOptions.hasFormat(Format.ACTION_SUGGEST)) break;
                    actions.add(MessageAction.from(Format.ACTION_SUGGEST, entry.getValue()));
                    break;

                case "clipboard":
                    if (!messageOptions.hasFormat(Format.ACTION_CLIPBOARD)) break;
                    actions.add(MessageAction.from(Format.ACTION_CLIPBOARD, entry.getValue()));
                    break;

                case "url":
                    if (!messageOptions.hasFormat(Format.ACTION_URL)) break;
                    actions.add(MessageAction.from(Format.ACTION_URL, entry.getValue()));
                    break;
            }
        }

        this.actions = actions;
        visitChildren(action);
        this.actions = null;
    }

    @Override
    public void visit(final LineBreak lineBreak) {
        if (!messageOptions.hasFormat(Format.NEW_LINE)) {
            appendNode("\\n");
            visitChildren(lineBreak);
            return;
        }

        nodes.add(new LineBreakNode());
        visitChildren(lineBreak);
    }

    /**
     * Handles the appending of the component
     *
     * @param text A parsed {@link Text} node
     */
    @Override
    public void visit(final Text text) {

        if (replaceable != null) {
            final ReplaceableNode node = new ReplaceableNode(text.getLiteral());
            // TODO HAVE THE EXTENSION GET HERE
            nodes.add(node);
            visitChildren(text);
            return;
        }

        appendNode(text.getLiteral());
        visitChildren(text);
    }

    private void appendNode(@NotNull final String literal) {
        final TextNode messageNode = new TextNode(literal);

        messageNode.setColor(currentColor);

        if (bold) messageNode.setBold(true);
        if (italic) messageNode.setItalic(true);
        if (strike) messageNode.setStrike(true);
        if (underline) messageNode.setUnderlined(true);
        if (obfuscated) messageNode.setObfuscated(true);

        if (actions != null) messageNode.setActions(actions);

        nodes.add(messageNode);
    }

}