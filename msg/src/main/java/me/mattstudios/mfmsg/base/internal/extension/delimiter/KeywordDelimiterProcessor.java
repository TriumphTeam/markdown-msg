package me.mattstudios.mfmsg.base.internal.extension.delimiter;

import me.mattstudios.mfmsg.base.internal.extension.node.KeywordNode;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterRun;

public final class KeywordDelimiterProcessor implements DelimiterProcessor {

    @Override
    public char getOpeningCharacter() {
        return '{';
    }

    @Override
    public char getClosingCharacter() {
        return '}';
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public int getDelimiterUse(final DelimiterRun opener, final DelimiterRun closer) {
        return opener.length() >= 1 && closer.length() >= 1 ? 1 : 0;
    }

    @Override
    public void process(final Text opener, final Text closer, final int delimiterCount) {
        // Wrap nodes between delimiters in strikethrough.
        final Node keyword = new KeywordNode();

        Node tmp = opener.getNext();
        while (tmp != null && !tmp.equals(closer)) {
            final Node next = tmp.getNext();
            keyword.appendChild(tmp);
            tmp = next;
        }

        opener.insertAfter(keyword);
    }

}
