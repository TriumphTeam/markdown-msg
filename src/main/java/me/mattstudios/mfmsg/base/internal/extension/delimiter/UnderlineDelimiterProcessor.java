package me.mattstudios.mfmsg.base.internal.extension.delimiter;

import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;

public final class UnderlineDelimiterProcessor implements DelimiterProcessor {

    @Override
    public char getOpeningCharacter() {
        return '_';
    }

    @Override
    public char getClosingCharacter() {
        return '_';
    }

    @Override
    public int getMinLength() {
        return 2;
    }

    @Override
    public int getDelimiterUse(final DelimiterRun opener, final DelimiterRun closer) {
        return opener.length() >= 2 && closer.length() >= 2 ? 2 : 0;
    }

    @Override
    public void process(final Text opener, final Text closer, final int delimiterCount) {
        // Wrap nodes between delimiters in strikethrough.
        final Node underline = new Underline();

        Node tmp = opener.getNext();
        while (tmp != null && tmp != closer) {
            final Node next = tmp.getNext();
            underline.appendChild(tmp);
            tmp = next;
        }

        opener.insertAfter(underline);
    }

}