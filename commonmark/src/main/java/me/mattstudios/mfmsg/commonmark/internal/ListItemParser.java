package me.mattstudios.mfmsg.commonmark.internal;

import me.mattstudios.mfmsg.commonmark.node.Block;
import me.mattstudios.mfmsg.commonmark.node.ListBlock;
import me.mattstudios.mfmsg.commonmark.node.ListItem;
import me.mattstudios.mfmsg.commonmark.node.Paragraph;
import me.mattstudios.mfmsg.commonmark.parser.block.AbstractBlockParser;
import me.mattstudios.mfmsg.commonmark.parser.block.BlockContinue;
import me.mattstudios.mfmsg.commonmark.parser.block.ParserState;

public class ListItemParser extends AbstractBlockParser {

    private final ListItem block = new ListItem();

    /**
     * Minimum number of columns that the content has to be indented (relative to the containing block) to be part of
     * this list item.
     */
    private int contentIndent;

    private boolean hadBlankLine;

    public ListItemParser(int contentIndent) {
        this.contentIndent = contentIndent;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block childBlock) {
        if (hadBlankLine) {
            // We saw a blank line in this list item, that means the list block is loose.
            //
            // spec: if any of its constituent list items directly contain two block-level elements with a blank line
            // between them
            Block parent = block.getParent();
            if (parent instanceof ListBlock) {
                ((ListBlock) parent).setTight(false);
            }
        }
        return true;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        if (state.isBlank()) {
            if (block.getFirstChild() == null) {
                // Blank line after empty list item
                return BlockContinue.none();
            } else {
                Block activeBlock = state.getActiveBlockParser().getBlock();
                // If the active block is a code block, blank lines in it should not affect if the list is tight.
                hadBlankLine = activeBlock instanceof Paragraph || activeBlock instanceof ListItem;
                return BlockContinue.atIndex(state.getNextNonSpaceIndex());
            }
        }

        if (state.getIndent() >= contentIndent) {
            return BlockContinue.atColumn(state.getColumn() + contentIndent);
        } else {
            return BlockContinue.none();
        }
    }
}
