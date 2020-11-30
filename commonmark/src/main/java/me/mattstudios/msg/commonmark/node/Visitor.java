package me.mattstudios.msg.commonmark.node;

import me.mattstudios.msg.commonmark.node.mf.Action;
import me.mattstudios.msg.commonmark.node.mf.Color;
import me.mattstudios.msg.commonmark.node.mf.Gradient;
import me.mattstudios.msg.commonmark.node.mf.LegacyBold;
import me.mattstudios.msg.commonmark.node.mf.LegacyItalic;
import me.mattstudios.msg.commonmark.node.mf.LegacyObfuscated;
import me.mattstudios.msg.commonmark.node.mf.LegacyStrikethrough;
import me.mattstudios.msg.commonmark.node.mf.LegacyUnderline;
import me.mattstudios.msg.commonmark.node.mf.LineBreak;
import me.mattstudios.msg.commonmark.node.mf.Rainbow;
import me.mattstudios.msg.commonmark.node.mf.Reset;
import org.jetbrains.annotations.NotNull;

/**
 * Node visitor.
 * <p>
 * Implementations should subclass {@link AbstractVisitor} instead of implementing this directly.
 */
public interface Visitor {

    /* START MF NODES */

    void visit(@NotNull final Color color);

    void visit(@NotNull final Reset reset);

    void visit(@NotNull final Rainbow rainbow);

    void visit(@NotNull final Gradient gradient);

    void visit(@NotNull final Action action);

    void visit(@NotNull final LineBreak lineBreak);

    // Legacy

    void visit(@NotNull final LegacyBold legacyBold);

    void visit(@NotNull final LegacyItalic legacyItalic);

    void visit(@NotNull final LegacyStrikethrough legacyStrikethrough);

    void visit(@NotNull final LegacyUnderline legacyUnderline);

    void visit(@NotNull final LegacyObfuscated legacyObfuscated);

    /* END MF NODES */

    void visit(@NotNull final Document document);

    void visit(@NotNull final Emphasis emphasis);

    void visit(@NotNull final Paragraph paragraph);

    void visit(@NotNull final StrongEmphasis strongEmphasis);

    void visit(@NotNull final Text text);

    void visit(@NotNull final CustomBlock customBlock);

    void visit(@NotNull final CustomNode customNode);
}
