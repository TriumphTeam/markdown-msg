package me.mattstudios.mfmsg.base.internal.extensions.node;

import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Delimited;

public final class Underline extends CustomNode implements Delimited {

    private static final String DELIMITER = "__";

    @Override
    public String getOpeningDelimiter() {
        return DELIMITER;
    }

    @Override
    public String getClosingDelimiter() {
        return DELIMITER;
    }

}
