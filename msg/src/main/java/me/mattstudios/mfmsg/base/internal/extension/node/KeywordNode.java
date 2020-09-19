package me.mattstudios.mfmsg.base.internal.extension.node;

import me.mattstudios.mfmsg.commonmark.node.CustomNode;
import me.mattstudios.mfmsg.commonmark.node.Delimited;

public final class KeywordNode extends CustomNode implements Delimited {

    private static final String OPEN_DELIMITER = "{";
    private static final String CLOSE_DELIMITER = "}";

    @Override
    public String getOpeningDelimiter() {
        return OPEN_DELIMITER;
    }

    @Override
    public String getClosingDelimiter() {
        return CLOSE_DELIMITER;
    }

}
