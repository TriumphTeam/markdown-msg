package me.mattstudios.mfmsg.base.internal.extension.node;

import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Delimited;

public final class Replaceable extends CustomNode implements Delimited {

    private final char openingCharacter;
    private final char closingCharacter;

    public Replaceable(final char openingCharacter, final char closingCharacter) {
        this.openingCharacter = openingCharacter;
        this.closingCharacter = closingCharacter;
    }

    @Override
    public String getOpeningDelimiter() {
        return String.valueOf(openingCharacter);
    }

    @Override
    public String getClosingDelimiter() {
        return String.valueOf(closingCharacter);
    }

}
