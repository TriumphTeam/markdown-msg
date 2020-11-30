package me.mattstudios.msg.base.internal.extensions.node;

import me.mattstudios.msg.commonmark.node.CustomNode;
import me.mattstudios.msg.commonmark.node.Delimited;

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
