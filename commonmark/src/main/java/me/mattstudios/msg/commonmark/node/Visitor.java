package me.mattstudios.msg.commonmark.node;

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
