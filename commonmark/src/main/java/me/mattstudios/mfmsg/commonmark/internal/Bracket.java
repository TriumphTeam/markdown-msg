package me.mattstudios.mfmsg.commonmark.internal;

import me.mattstudios.mfmsg.commonmark.internal.inline.Position;
import me.mattstudios.mfmsg.commonmark.node.Text;

/**
 * Opening bracket for links (<code>[</code>) or images (<code>![</code>).
 */
public class Bracket {

    public final Text node;
    /**
     * The position of the content (after the opening bracket)
     */
    public final Position contentPosition;
    public final boolean image;

    /**
     * Previous bracket.
     */
    public final Bracket previous;

    /**
     * Previous delimiter (emphasis, etc) before this bracket.
     */
    public final Delimiter previousDelimiter;

    /**
     * Whether this bracket is allowed to form a link/image (also known as "active").
     */
    public boolean allowed = true;

    /**
     * Whether there is an unescaped bracket (opening or closing) anywhere after this opening bracket.
     */
    public boolean bracketAfter = false;

    static public Bracket link(Text node, Position contentPosition, Bracket previous, Delimiter previousDelimiter) {
        return new Bracket(node, contentPosition, previous, previousDelimiter, false);
    }

    static public Bracket image(Text node, Position contentPosition, Bracket previous, Delimiter previousDelimiter) {
        return new Bracket(node, contentPosition, previous, previousDelimiter, true);
    }

    private Bracket(Text node, Position contentPosition, Bracket previous, Delimiter previousDelimiter, boolean image) {
        this.node = node;
        this.contentPosition = contentPosition;
        this.image = image;
        this.previous = previous;
        this.previousDelimiter = previousDelimiter;
    }
}
