package me.mattstudios.msg.base.internal.renderer;

import me.mattstudios.msg.base.MessageOptions;
import me.mattstudios.msg.base.internal.Format;
import me.mattstudios.msg.base.internal.action.MessageAction;
import me.mattstudios.msg.base.internal.color.MessageColor;
import me.mattstudios.msg.base.internal.components.LineBreakNode;
import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.internal.components.TextNode;
import me.mattstudios.msg.base.internal.extensions.node.Obfuscated;
import me.mattstudios.msg.base.internal.extensions.node.Strikethrough;
import me.mattstudios.msg.base.internal.extensions.node.Underline;
import me.mattstudios.msg.base.internal.parser.MarkdownParser;
import me.mattstudios.msg.commonmark.node.AbstractVisitor;
import me.mattstudios.msg.commonmark.node.CustomNode;
import me.mattstudios.msg.commonmark.node.Emphasis;
import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.StrongEmphasis;
import me.mattstudios.msg.commonmark.node.Text;
import me.mattstudios.msg.commonmark.node.triumph.Action;
import me.mattstudios.msg.commonmark.node.triumph.Color;
import me.mattstudios.msg.commonmark.node.triumph.Gradient;
import me.mattstudios.msg.commonmark.node.triumph.LegacyBold;
import me.mattstudios.msg.commonmark.node.triumph.LegacyItalic;
import me.mattstudios.msg.commonmark.node.triumph.LegacyObfuscated;
import me.mattstudios.msg.commonmark.node.triumph.LegacyStrikethrough;
import me.mattstudios.msg.commonmark.node.triumph.LegacyUnderline;
import me.mattstudios.msg.commonmark.node.triumph.LineBreak;
import me.mattstudios.msg.commonmark.node.triumph.Rainbow;
import me.mattstudios.msg.commonmark.node.triumph.Reset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class MarkdownRenderer extends AbstractVisitor {

    @NotNull
    private final MessageOptions messageOptions;

    @NotNull
    private final Map<Class<? extends CustomNode>, NodeRenderer> renderers;

    @NotNull
    private final List<MessageNode> nodes;

    // Text properties
    private boolean bold = false;
    private boolean italic = false;
    private boolean strike = false;
    private boolean underline = false;
    private boolean obfuscated = false;

    private boolean legacyBold = false;
    private boolean legacyItalic = false;
    private boolean legacyStrike = false;
    private boolean legacyUnderline = false;
    private boolean legacyObfuscated = false;

    @NotNull
    private MessageColor currentColor;
    @Nullable
    private List<MessageAction> actions = null;

    public MarkdownRenderer(
            @NotNull final List<MessageNode> nodes,
            @NotNull final MessageOptions messageOptions,
            @NotNull final Map<Class<? extends CustomNode>, NodeRenderer> renderers
    ) {
        this.nodes = nodes;
        this.messageOptions = messageOptions;
        this.currentColor = messageOptions.getDefaultColor();
        this.renderers = renderers;
    }

    /**
     * Visits the node's components
     *
     * @param node The node to visit
     *             //* @param appender The appender to append text to
     */
    public void visitComponents(@NotNull final Node node) {
        node.accept(this);
    }

    /**
     * Handles italic
     *
     * @param emphasis The italic is matched by either `* *` or `_ _`
     */
    @Override
    public void visit(@NotNull final Emphasis emphasis) {
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
    public void visit(@NotNull final StrongEmphasis strongEmphasis) {
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
    public void visit(@NotNull final CustomNode customNode) {
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

        final NodeRenderer nodeRenderer = renderers.get(customNode.getClass());
        if (nodeRenderer == null) {
            visitChildren(customNode);
            return;
        }

        nodes.add(nodeRenderer.render(customNode));
        visitChildren(customNode);
    }

    @Override
    public void visit(@NotNull final Color color) {
        if (color.isLegacy() && messageOptions.hasFormat(Format.COLOR)) {
            resetLegacyFormats();
            currentColor = MessageColor.from(color.getColor().charAt(0));
            visitChildren(color);
            return;
        }

        if (!messageOptions.hasFormat(Format.HEX)) {
            visitChildren(color);
            return;
        }

        resetLegacyFormats();
        currentColor = MessageColor.from(color.getColor());
        visitChildren(color);
    }

    @Override
    public void visit(@NotNull final LegacyBold legacyBold) {
        if (!messageOptions.hasFormat(Format.LEGACY_BOLD)) {
            visitChildren(legacyBold);
            return;
        }

        this.legacyBold = true;
        visitChildren(legacyBold);
    }

    @Override
    public void visit(@NotNull final LegacyItalic legacyItalic) {
        if (!messageOptions.hasFormat(Format.LEGACY_ITALIC)) {
            visitChildren(legacyItalic);
            return;
        }

        this.legacyItalic = true;
        visitChildren(legacyItalic);
    }

    @Override
    public void visit(final @NotNull LegacyStrikethrough legacyStrikethrough) {
        if (!messageOptions.hasFormat(Format.LEGACY_STRIKETHROUGH)) {
            visitChildren(legacyStrikethrough);
            return;
        }

        this.legacyStrike = true;
        visitChildren(legacyStrikethrough);
    }

    @Override
    public void visit(final @NotNull LegacyUnderline legacyUnderline) {
        if (!messageOptions.hasFormat(Format.LEGACY_UNDERLINE)) {
            visitChildren(legacyUnderline);
            return;
        }

        this.legacyUnderline = true;
        visitChildren(legacyUnderline);
    }

    @Override
    public void visit(final @NotNull LegacyObfuscated legacyObfuscated) {
        if (!messageOptions.hasFormat(Format.LEGACY_OBFUSCATED)) {
            visitChildren(legacyObfuscated);
            return;
        }

        this.legacyObfuscated = true;
        visitChildren(legacyObfuscated);
    }

    @Override
    public void visit(@NotNull final Reset reset) {
        currentColor = messageOptions.getDefaultColor();
        resetLegacyFormats();
        visitChildren(reset);
    }

    @Override
    public void visit(@NotNull final Rainbow rainbow) {
        if (!messageOptions.hasFormat(Format.RAINBOW)) {
            visitChildren(rainbow);
            return;
        }

        currentColor = MessageColor.from(rainbow.getSaturation(), rainbow.getBrightness());
        visitChildren(rainbow);
    }

    @Override
    public void visit(@NotNull final Gradient gradient) {
        if (!messageOptions.hasFormat(Format.GRADIENT)) {
            visitChildren(gradient);
            return;
        }

        currentColor = MessageColor.from(gradient.getHexes());
        visitChildren(gradient);
    }

    @Override
    public void visit(@NotNull final Action action) {
        final List<MessageAction> actions = new ArrayList<>();
        for (final Entry<String, String> entry : action.getActions().entrySet()) {
            switch (entry.getKey().toLowerCase()) {
                case "hover":
                    if (!messageOptions.hasFormat(Format.ACTION_HOVER)) break;
                    final MarkdownParser parser = new MarkdownParser(messageOptions);
                    actions.add(MessageAction.from(parser.parse(entry.getValue())));
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

        if (!actions.isEmpty()) this.actions = actions;
        visitChildren(action);
        this.actions = null;
    }

    @Override
    public void visit(@NotNull final LineBreak lineBreak) {
        if (!messageOptions.hasFormat(Format.LINE_BREAK)) {
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
    public void visit(@NotNull final Text text) {
        appendNode(text.getLiteral());
        visitChildren(text);
    }

    private void appendNode(@NotNull final String literal) {
        final TextNode messageNode = new TextNode(literal);

        messageNode.setColor(currentColor);

        if (bold || legacyBold) messageNode.setBold(true);
        if (italic || legacyItalic) messageNode.setItalic(true);
        if (strike || legacyStrike) messageNode.setStrike(true);
        if (underline || legacyUnderline) messageNode.setUnderlined(true);
        if (obfuscated || legacyObfuscated) messageNode.setObfuscated(true);

        if (actions != null) messageNode.setActions(actions);

        nodes.add(messageNode);
    }

    private void resetLegacyFormats() {
        legacyBold = false;
        legacyItalic = false;
        legacyStrike = false;
        legacyUnderline = false;
        legacyObfuscated = false;
    }

}