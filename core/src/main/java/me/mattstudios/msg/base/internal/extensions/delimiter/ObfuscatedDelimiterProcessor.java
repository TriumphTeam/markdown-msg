package me.mattstudios.msg.base.internal.extensions.delimiter;

import me.mattstudios.msg.base.internal.extensions.node.Obfuscated;
import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Text;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterProcessor;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterRun;

public final class ObfuscatedDelimiterProcessor implements DelimiterProcessor {

    @Override
    public char getOpeningCharacter() {
        return '|';
    }

    @Override
    public char getClosingCharacter() {
        return '|';
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
        final Node obfuscated = new Obfuscated();

        Node tmp = opener.getNext();
        while (tmp != null && !tmp.equals(closer)) {
            final Node next = tmp.getNext();
            obfuscated.appendChild(tmp);
            tmp = next;
        }

        opener.insertAfter(obfuscated);
    }

}
