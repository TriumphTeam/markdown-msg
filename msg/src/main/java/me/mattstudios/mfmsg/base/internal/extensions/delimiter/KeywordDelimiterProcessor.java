package me.mattstudios.mfmsg.base.internal.extensions.delimiter;

import me.mattstudios.mfmsg.base.internal.extensions.node.Replaceable;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterRun;

public final class KeywordDelimiterProcessor implements DelimiterProcessor {

    private final char openingCharacter;
    private final char closingCharacter;
    private final int amount;

    public KeywordDelimiterProcessor(final char openingCharacter, final char closingCharacter, final int amount) {
        this.openingCharacter = openingCharacter;
        this.closingCharacter = closingCharacter;
        this.amount = amount;
    }

    @Override
    public char getOpeningCharacter() {
        return openingCharacter;
    }

    @Override
    public char getClosingCharacter() {
        return closingCharacter;
    }

    @Override
    public int getMinLength() {
        return 1;
    }

    @Override
    public int getDelimiterUse(final DelimiterRun opener, final DelimiterRun closer) {
        return opener.length() >= amount && closer.length() >= amount ? amount : 0;
    }

    @Override
    public void process(final Text opener, final Text closer, final int delimiterCount) {
        // Wrap nodes between delimiters in strikethrough.
        final Node keyword = new Replaceable(openingCharacter, closingCharacter);
        Node tmp = opener.getNext();
        while (tmp != null && !tmp.equals(closer)) {
            final Node next = tmp.getNext();
            keyword.appendChild(tmp);
            tmp = next;
        }

        opener.insertAfter(keyword);
    }

}
