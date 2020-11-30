package me.mattstudios.msg.commonmark.parser.block;

import me.mattstudios.msg.commonmark.node.Block;
import me.mattstudios.msg.commonmark.node.SourceSpan;
import me.mattstudios.msg.commonmark.parser.InlineParser;

public abstract class AbstractBlockParser implements BlockParser {

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean canHaveLazyContinuationLines() {
        return false;
    }

    @Override
    public boolean canContain(Block childBlock) {
        return false;
    }

    @Override
    public void addLine(CharSequence line) {
    }

    @Override
    public void addSourceSpan(SourceSpan sourceSpan) {
        getBlock().addSourceSpan(sourceSpan);
    }

    @Override
    public void closeBlock() {
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
    }

}
