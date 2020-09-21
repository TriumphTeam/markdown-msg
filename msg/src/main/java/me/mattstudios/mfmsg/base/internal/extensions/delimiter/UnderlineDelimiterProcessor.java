package me.mattstudios.mfmsg.base.internal.extension.delimiter;

import me.mattstudios.mfmsg.base.internal.extension.node.Underline;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterRun;

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
        while (tmp != null && !tmp.equals(closer)) {
            final Node next = tmp.getNext();
            underline.appendChild(tmp);
            tmp = next;
        }

        opener.insertAfter(underline);
    }

}
