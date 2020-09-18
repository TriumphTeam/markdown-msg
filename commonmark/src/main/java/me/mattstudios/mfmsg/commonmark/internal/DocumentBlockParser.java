package me.mattstudios.mfmsg.commonmark.internal;

import me.mattstudios.mfmsg.commonmark.node.Block;
import me.mattstudios.mfmsg.commonmark.node.Document;
import me.mattstudios.mfmsg.commonmark.parser.block.AbstractBlockParser;
import me.mattstudios.mfmsg.commonmark.parser.block.BlockContinue;
import me.mattstudios.mfmsg.commonmark.parser.block.ParserState;

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
