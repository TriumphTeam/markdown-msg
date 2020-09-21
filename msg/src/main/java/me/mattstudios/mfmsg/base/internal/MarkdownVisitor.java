package me.mattstudios.mfmsg.base.internal;

import me.mattstudios.mfmsg.base.internal.action.ClickMessageAction;
import me.mattstudios.mfmsg.base.internal.action.HoverMessageAction;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.FlatColor;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.TextNode;
import me.mattstudios.mfmsg.base.internal.component.LineBreakNode;
import me.mattstudios.mfmsg.base.internal.component.MessageNode;
import me.mattstudios.mfmsg.base.internal.component.ReplaceableNode;
import me.mattstudios.mfmsg.base.internal.extension.node.KeywordNode;
import me.mattstudios.mfmsg.base.internal.extension.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extension.node.Strikethrough;
import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class MarkdownVisitor extends AbstractVisitor {

    private final Set<Format> formats;

    // Text properties
    private boolean bold = false;
    private boolean italic = false;
    private boolean strike = false;
    private boolean underline = false;
    private boolean obfuscated = false;

    private boolean replaceable = false;

    private List<MessageNode> nodes;

    private MessageColor currentColor = new FlatColor("white");
    private List<MessageAction> actions = null;

    public MarkdownVisitor(final Set<Format> formats) {
        this.formats = formats;
    }

    /**
     * Visits the node's components
     *
     * @param node The node to visit
     *             //* @param appender The appender to append text to
     */
    public void visitComponents(final Node node, final List<MessageNode> nodes) {
        if (this.nodes != nodes) this.nodes = nodes;
        node.accept(this);
    }

    /**
     * Handles italic
     *
     * @param emphasis The italic is matched by either `* *` or `_ _`
     */
    @Override
    public void visit(final Emphasis emphasis) {
        if (!formats.contains(Format.ITALIC)) {
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
        if (!formats.contains(Format.BOLD)) {
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
            if (!formats.contains(Format.UNDERLINE)) {
                visitChildren(customNode);
                return;
            }
            underline = true;
            visitChildren(customNode);
            underline = false;
            return;
        }

        if (customNode instanceof Obfuscated) {
            if (!formats.contains(Format.OBFUSCATED)) {
                visitChildren(customNode);
                return;
            }
            obfuscated = true;
            visitChildren(customNode);
            obfuscated = false;
            return;
        }

        if (customNode instanceof Strikethrough) {
            if (!formats.contains(Format.STRIKETHROUGH)) {
                visitChildren(customNode);
                return;
            }

            strike = true;
            visitChildren(customNode);
            strike = false;
        }

        if (customNode instanceof KeywordNode) {
            replaceable = true;
            visitChildren(customNode);
            replaceable = false;
        }

    }

    @Override
    public void visit(final Color color) {
        if (color.isLegacy()) currentColor = MessageColor.of(color.getColor().charAt(0));
        else currentColor = MessageColor.of(color.getColor());
        visitChildren(color);
    }

    @Override
    public void visit(final Reset reset) {
        currentColor = MessageColor.of("white");
        visitChildren(reset);
    }

    @Override
    public void visit(final Rainbow rainbow) {
        currentColor = MessageColor.of(rainbow.getSaturation(), rainbow.getBrightness());
        visitChildren(rainbow);
    }

    @Override
    public void visit(final Gradient gradient) {
        currentColor = MessageColor.of(gradient.getHexes());
        visitChildren(gradient);
    }

    @Override
    public void visit(final Action action) {
        final List<MessageAction> actions = new ArrayList<>();
        for (final Entry<String, String> entry : action.getActions().entrySet()) {
            switch (entry.getKey().toLowerCase()) {
                case "hover":
                    if (!formats.contains(Format.ACTION_HOVER)) break;
                    final MessageParser parser = new MessageParser(formats, new FlatColor("white"));
                    parser.parse(entry.getValue());
                    actions.add(new HoverMessageAction(parser.build()));
                    break;

                case "command":
                    if (!formats.contains(Format.ACTION_COMMAND)) break;
                    actions.add(new ClickMessageAction(Format.ACTION_COMMAND, entry.getValue()));
                    break;

                case "suggest":
                    if (!formats.contains(Format.ACTION_SUGGEST)) break;
                    actions.add(new ClickMessageAction(Format.ACTION_SUGGEST, entry.getValue()));
                    break;

                case "clipboard":
                    if (!formats.contains(Format.ACTION_CLIPBOARD)) break;
                    actions.add(new ClickMessageAction(Format.ACTION_CLIPBOARD, entry.getValue()));
                    break;

                case "url":
                    if (!formats.contains(Format.ACTION_URL)) break;
                    actions.add(new ClickMessageAction(Format.ACTION_URL, entry.getValue()));
                    break;
            }
        }

        this.actions = actions;
        visitChildren(action);
        this.actions = null;
    }

    @Override
    public void visit(final LineBreak lineBreak) {
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

        if (replaceable) {
            final ReplaceableNode node = new ReplaceableNode(text.getLiteral());
            nodes.add(node);
            visitChildren(text);
            return;
        }

        final TextNode messageNode = new TextNode(text.getLiteral());

        messageNode.setColor(currentColor);

        if (bold) messageNode.setBold(true);
        if (italic) messageNode.setItalic(true);
        if (strike) messageNode.setStrike(true);
        if (underline) messageNode.setUnderlined(true);
        if (obfuscated) messageNode.setObfuscated(true);

        if (actions != null) messageNode.setActions(actions);

        nodes.add(messageNode);

        visitChildren(text);
    }

}