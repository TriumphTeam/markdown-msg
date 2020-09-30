package me.mattstudios.mfmsg.base.internal.renderer;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.components.LineBreakNode;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.components.TextNode;
import me.mattstudios.mfmsg.base.internal.extensions.ReplaceableHandler;
import me.mattstudios.mfmsg.base.internal.extensions.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extensions.node.Replaceable;
import me.mattstudios.mfmsg.base.internal.extensions.node.Strikethrough;
import me.mattstudios.mfmsg.base.internal.extensions.node.Underline;
import me.mattstudios.mfmsg.base.internal.parser.MarkdownParser;
import me.mattstudios.mfmsg.commonmark.node.AbstractVisitor;
import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Emphasis;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.StrongEmphasis;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.node.mf.Action;
import me.mattstudios.mfmsg.commonmark.node.mf.Color;
import me.mattstudios.mfmsg.commonmark.node.mf.Gradient;
import me.mattstudios.mfmsg.commonmark.node.mf.LegacyBold;
import me.mattstudios.mfmsg.commonmark.node.mf.LegacyItalic;
import me.mattstudios.mfmsg.commonmark.node.mf.LegacyObfuscated;
import me.mattstudios.mfmsg.commonmark.node.mf.LegacyStrikethrough;
import me.mattstudios.mfmsg.commonmark.node.mf.LegacyUnderline;
import me.mattstudios.mfmsg.commonmark.node.mf.LineBreak;
import me.mattstudios.mfmsg.commonmark.node.mf.Rainbow;
import me.mattstudios.mfmsg.commonmark.node.mf.Reset;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public final class MarkdownRenderer extends AbstractVisitor {

    @NotNull
    private final MessageOptions messageOptions;

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

    @Nullable
    private Replaceable replaceable = null;

    @NotNull
    private MessageColor currentColor;
    @Nullable
    private List<MessageAction> actions = null;

    public MarkdownRenderer(@NotNull final List<MessageNode> nodes, @NotNull final MessageOptions messageOptions) {
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

        if (customNode instanceof Replaceable) {
            replaceable = (Replaceable) customNode;
            visitChildren(customNode);
            replaceable = null;
        }

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

        if (!actions.isEmpty()) this.actions = actions;
        visitChildren(action);
        this.actions = null;
    }

    @Override
    public void visit(@NotNull final LineBreak lineBreak) {
        if (!messageOptions.hasFormat(Format.NEW_LINE)) {
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
        if (replaceable != null) {
            final ReplaceableHandler replaceableHandler = messageOptions.getReplaceableHandler();

            // Likely never happen
            if (replaceableHandler == null) {
                appendNode(text.getLiteral());
                visitChildren(text);
                return;
            }

            final MessageNode node = replaceableHandler.getNode(text.getLiteral());

            if (node == null) {
                appendNode(replaceableHandler.getOpener() + text.getLiteral() + replaceableHandler.getCloser());
                visitChildren(text);
                return;
            }

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