package me.mattstudios.mfmsg.base.internal.extension.node;

import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Delimited;

public final class Obfuscated extends CustomNode implements Delimited {

    private static final String DELIMITER = "||";

    @Override
    public String getOpeningDelimiter() {
        return DELIMITER;
    }

    @Override
    public String getClosingDelimiter() {
        return DELIMITER;
    }

}
