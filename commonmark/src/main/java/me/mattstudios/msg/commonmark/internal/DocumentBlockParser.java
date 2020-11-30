package me.mattstudios.msg.commonmark.internal;

import me.mattstudios.msg.commonmark.node.Block;
import me.mattstudios.msg.commonmark.node.Document;
import me.mattstudios.msg.commonmark.parser.block.AbstractBlockParser;
import me.mattstudios.msg.commonmark.parser.block.BlockContinue;
import me.mattstudios.msg.commonmark.parser.block.ParserState;

public class DocumentBlockParser extends AbstractBlockParser {

    private final Document document = new Document();

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return true;
    }

    @Override
    public Document getBlock() {
        return document;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        return BlockContinue.atIndex(state.getIndex());
    }

    @Override
    public void addLine(CharSequence line) {
    }

}
