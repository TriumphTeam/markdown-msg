package me.mattstudios.mfmsg.base.internal.extension.node;

import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;

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
