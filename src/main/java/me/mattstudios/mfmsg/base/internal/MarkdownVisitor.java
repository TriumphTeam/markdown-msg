package me.mattstudios.mfmsg.base.internal;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.commonmark.ext.gfm.strikethrough.Strikethrough;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class MarkdownVisitor extends AbstractVisitor {

    @NotNull
    private final ComponentBuilder builder = new ComponentBuilder();

    private final List<HoverEvent> hoverEvents = new ArrayList<>();
    private final List<ClickEvent> clickEvents = new ArrayList<>();

    private boolean italic;
    private boolean bold;
    private boolean strike;

    public Set<Class<? extends Node>> getNodeTypes() {
        return ImmutableSet.of(Document.class, Paragraph.class);
    }

    public void render(final Node node) {
        node.accept(this);
    }

    @Override
    public void visit(final Document document) {
        visitChildren(document);
    }

    @Override
    public void visit(final Emphasis emphasis) {
        italic = true;
        visitChildren(emphasis);
        italic = false;
    }

    @Override
    public void visit(final StrongEmphasis strongEmphasis) {
        bold = true;
        visitChildren(strongEmphasis);
        bold = false;
    }

    @Override
    public void visit(final CustomNode customNode) {
        if (!(customNode instanceof Strikethrough)) return;

        strike = true;
        visitChildren(customNode);
        strike = false;
    }

    @Override
    public void visit(final Text text) {
        builder.append(TextComponent.fromLegacyText(text.getLiteral()), ComponentBuilder.FormatRetention.NONE);

        builder.italic(italic);
        builder.bold(bold);
        builder.strikethrough(strike);

        for (final HoverEvent event : hoverEvents) {
            builder.event(event);
        }

        for (final ClickEvent event : clickEvents) {
            builder.event(event);
        }

        visitChildren(text);
    }

    public void addHoverEvents(final List<HoverEvent> hoverEvents) {
        this.hoverEvents.addAll(hoverEvents);
    }

    public void addClickEvents(final List<ClickEvent> clickEvents) {
        this.clickEvents.addAll(clickEvents);
    }

    public BaseComponent[] build() {
        final BaseComponent[] component = builder.create();
        reset();
        return component;
    }

    private boolean currentHasText() {
        final BaseComponent current = builder.getCurrentComponent();
        return current instanceof TextComponent && !((TextComponent) current).getText().isEmpty();
    }

    private void reset() {
        builder.getParts().clear();

        hoverEvents.clear();
        clickEvents.clear();
    }

}