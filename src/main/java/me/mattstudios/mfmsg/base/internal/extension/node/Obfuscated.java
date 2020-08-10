package me.mattstudios.mfmsg.base.internal.extension.node;

import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;

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
