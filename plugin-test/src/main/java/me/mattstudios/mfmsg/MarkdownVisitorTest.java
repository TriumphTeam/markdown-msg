package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.extension.node.KeywordNode;
import me.mattstudios.mfmsg.base.internal.extension.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extension.node.Strikethrough;
import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
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

import java.util.Map;
import java.util.Set;

public final class MarkdownVisitorTest extends AbstractVisitor {

    private final Set<Format> formats;

    // Text properties
    private boolean bold;
    private boolean italic;
    private boolean strike;
    private boolean underline;
    private boolean obfuscated;

    private Appender appender;

    private String currentColor = "white";
    private Map<String,String> actions = null;

    private boolean lineBreak;

    public MarkdownVisitorTest(final Set<Format> formats) {
        this.formats = formats;
    }

    /**
     * Visits the node's components
     *
     * @param node     The node to visit
     * @param appender The appender to append text to
     */
    public void visitComponents(final Node node, final Appender appender) {
        //if (this.appender != appender) this.appender = appender;
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
            System.out.println(customNode);
            visitChildren(customNode);
        }

    }

    @Override
    public void visit(final Color color) {
        currentColor = color.getColor();
        visitChildren(color);
        //currentColor = "white";
    }

    @Override
    public void visit(final Reset reset) {
        currentColor = "white";
        visitChildren(reset);
    }

    @Override
    public void visit(final Rainbow rainbow) {
        currentColor = "RAINBOW";
        //System.out.println("sat: " + rainbow.getSaturation() + " bri: " + rainbow.getBrightness());
        visitChildren(rainbow);
    }

    @Override
    public void visit(final Gradient gradient) {
        currentColor = "GRADIENT";
        //System.out.println(gradient.getHexes());
        visitChildren(gradient);
    }

    @Override
    public void visit(final Action action) {
        actions = action.getActions();
        visitChildren(action);
        actions = null;
    }

    @Override
    public void visit(final LineBreak lineBreak) {
        this.lineBreak = true;
        visitChildren(lineBreak);
        this.lineBreak = false;
    }

    /**
     * Handles the appending of the component
     *
     * @param text A parsed {@link Text} node
     */
    @Override
    public void visit(final Text text) {
        //appender.append(text.getLiteral(), italic, bold, strike, underline, obfuscated);
        System.out.println(text.getLiteral() + " -> " + italic + " - " + bold + " - " + strike + " - " + underline + " - " + obfuscated + " - " + currentColor + " - " + actions);
        visitChildren(text);
    }

}