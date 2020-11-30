package me.mattstudios.msg.base.internal.extensions.delimiter;

import me.mattstudios.msg.base.internal.extensions.node.Strikethrough;
import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Text;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterProcessor;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterRun;
import org.jetbrains.annotations.NotNull;

public final class StrikethroughDelimiterProcessor implements DelimiterProcessor {

    @Override
    public char getOpeningCharacter() {
        return '~';
    }

    @Override
    public char getClosingCharacter() {
        return '~';
    }

    @Override
    public int getMinLength() {
        return 2;
    }

    @Override
    public int getDelimiterUse(@NotNull final DelimiterRun opener, @NotNull final DelimiterRun closer) {
        return opener.length() >= 2 && closer.length() >= 2 ? 2 : 0;
    }

    @Override
    public void process(@NotNull final Text opener, @NotNull final Text closer, final int delimiterCount) {
        // Wrap nodes between delimiters in strikethrough.
        Node strikethrough = new Strikethrough();

        Node tmp = opener.getNext();
        while (tmp != null && !tmp.equals(closer)) {
            final Node next = tmp.getNext();
            strikethrough.appendChild(tmp);
            tmp = next;
        }

        opener.insertAfter(strikethrough);
    }

}
