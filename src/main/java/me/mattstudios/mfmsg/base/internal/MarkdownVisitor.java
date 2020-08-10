package me.mattstudios.mfmsg.base.internal;

import me.mattstudios.mfmsg.base.internal.extension.node.Obfuscated;
import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Emphasis;
import org.commonmark.node.Node;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MarkdownVisitor extends AbstractVisitor {

    @NotNull
    private final ComponentBuilder builder = new ComponentBuilder();

    @Nullable
    private HoverEvent hoverEvent = null;
    @Nullable
    private ClickEvent clickEvent = null;

    // Text properties
    private boolean italic;
    private boolean bold;
    private boolean strike;
    private boolean underLine;
    private boolean obfuscated;

    /**
     * Parses the {@link Node}
     *
     * @param node The {@link Node} given by the {@link Parser}
     */
    public void parse(final Node node) {
        node.accept(this);
    }

    /**
     * Handles italic
     *
     * @param emphasis The italic is matched by either `* *` or `_ _`
     */
    @Override
    public void visit(final Emphasis emphasis) {
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
            underLine = true;
            visitChildren(customNode);
            underLine = false;
            return;
        }

        if (customNode instanceof Obfuscated) {
            obfuscated = true;
            visitChildren(customNode);
            obfuscated = false;
            return;
        }

        if (!(customNode instanceof Strikethrough)) return;
        strike = true;
        visitChildren(customNode);
        strike = false;
    }

    /**
     * Handles the appending of the component
     *
     * @param text A parsed {@link Text} node
     */
    @Override
    public void visit(final Text text) {
        append(TextComponent.fromLegacyText(text.getLiteral()));

        // Sets each property accordingly
        builder.italic(italic);
        builder.bold(bold);
        builder.strikethrough(strike);
        builder.underlined(underLine);
        builder.obfuscated(obfuscated);

        // If available sets the event
        if (hoverEvent != null) builder.event(hoverEvent);
        if (clickEvent != null) builder.event(clickEvent);

        visitChildren(text);
    }

    /**
     * Sets the hover event to be used
     *
     * @param hoverEvent A nullable {@link HoverEvent}
     */
    public void setHoverEvent(@Nullable final HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    /**
     * Sets the click event to be used
     *
     * @param clickEvent A nullable {@link ClickEvent}
     */
    public void setClickEvent(@Nullable final ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    /**
     * Builds the component into a {@link BaseComponent[]} and resets the {@link #builder}
     *
     * @return A {@link BaseComponent[]} with all the parsed values
     */
    @NotNull
    public BaseComponent[] build() {
        final BaseComponent[] component = builder.create();
        reset();
        return component;
    }

    /**
     * Resets the {@link #builder}, {@link #hoverEvent}, and the {@link #clickEvent}
     */
    private void reset() {
        builder.getParts().clear();
        hoverEvent = null;
        clickEvent = null;
    }

    /**
     * Appends a the given {@link BaseComponent[]} into the {@link #builder}
     *
     * @param components A {@link BaseComponent[]}
     */
    private void append(@NotNull final BaseComponent[] components) {
        builder.append(components, ComponentBuilder.FormatRetention.NONE);
    }

}