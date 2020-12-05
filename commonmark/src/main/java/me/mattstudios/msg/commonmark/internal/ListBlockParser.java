package me.mattstudios.msg.commonmark.internal;

import me.mattstudios.msg.commonmark.internal.util.Parsing;
import me.mattstudios.msg.commonmark.node.*;
import me.mattstudios.msg.commonmark.parser.block.*;

public class ListBlockParser extends AbstractBlockParser {

    private final ListBlock block;

    private boolean hadBlankLine;
    private int linesAfterBlank;

    public ListBlockParser(ListBlock block) {
        this.block = block;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block childBlock) {
        if (childBlock instanceof ListItem) {
            // Another list item is added to this list block. If the previous line was blank, that means this list block
            // is "loose" (not tight).
            //
            // spec: A list is loose if any of its constituent list items are separated by blank lines
            if (hadBlankLine && linesAfterBlank == 1) {
                block.setTight(false);
                hadBlankLine = false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState state) {
        if (state.isBlank()) {
            hadBlankLine = true;
            linesAfterBlank = 0;
        } else if (hadBlankLine) {
            linesAfterBlank++;
        }
        // List blocks themselves don't have any markers, only list items. So try to stay in the list.
        // If there is a block start other than list item, canContain makes sure that this list is closed.
        return BlockContinue.atIndex(state.getIndex());
    }

    // spec: An ordered list marker is a sequence of 1–9 arabic digits (0-9), followed by either a `.` character or a
    // `)` character.
    private static ListMarkerData parseOrderedList(CharSequence line, int index) {
        int digits = 0;
        int length = line.length();
        for (int i = index; i < length; i++) {
            char c = line.charAt(i);
            switch (c) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    digits++;
                    if (digits > 9) {
                        return null;
                    }
                    break;
                case '.':
                case ')':
                    if (digits >= 1 && isSpaceTabOrEnd(line, i + 1)) {
                        String number = line.subSequence(index, i).toString();
                        OrderedList orderedList = new OrderedList();
                        orderedList.setStartNumber(Integer.parseInt(number));
                        orderedList.setDelimiter(c);
                        return new ListMarkerData(orderedList, i + 1);
                    } else {
                        return null;
                    }
                default:
                    return null;
            }
        }
        return null;
    }

    private static boolean isSpaceTabOrEnd(CharSequence line, int index) {
        if (index < line.length()) {
            switch (line.charAt(index)) {
                case ' ':
                case '\t':
                    return true;
                default:
                    return false;
            }
        } else {
            return true;
        }
    }

    private static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            BlockParser matched = matchedBlockParser.getMatchedBlockParser();

            if (state.getIndent() >= Parsing.CODE_BLOCK_INDENT) {
                return BlockStart.none();
            }
            int markerIndex = state.getNextNonSpaceIndex();
            int markerColumn = state.getColumn() + state.getIndent();
            boolean inParagraph = !matchedBlockParser.getParagraphLines().isEmpty();
            
            return BlockStart.none();
        }
    }

    private static class ListData {
        final ListBlock listBlock;
        final int contentColumn;

        ListData(ListBlock listBlock, int contentColumn) {
            this.listBlock = listBlock;
            this.contentColumn = contentColumn;
        }
    }

    private static class ListMarkerData {
        final ListBlock listBlock;
        final int indexAfterMarker;

        ListMarkerData(ListBlock listBlock, int indexAfterMarker) {
            this.listBlock = listBlock;
            this.indexAfterMarker = indexAfterMarker;
        }
    }
}
