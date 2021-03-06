package me.mattstudios.msg.commonmark.internal;

import me.mattstudios.msg.commonmark.internal.inline.triumph.TriggerProcessor;
import me.mattstudios.msg.commonmark.internal.util.Parsing;
import me.mattstudios.msg.commonmark.node.Block;
import me.mattstudios.msg.commonmark.node.Document;
import me.mattstudios.msg.commonmark.node.FencedCodeBlock;
import me.mattstudios.msg.commonmark.node.HtmlBlock;
import me.mattstudios.msg.commonmark.node.IndentedCodeBlock;
import me.mattstudios.msg.commonmark.node.LinkReferenceDefinition;
import me.mattstudios.msg.commonmark.node.ListBlock;
import me.mattstudios.msg.commonmark.node.Paragraph;
import me.mattstudios.msg.commonmark.node.SourceSpan;
import me.mattstudios.msg.commonmark.node.ThematicBreak;
import me.mattstudios.msg.commonmark.parser.IncludeSourceSpans;
import me.mattstudios.msg.commonmark.parser.InlineParser;
import me.mattstudios.msg.commonmark.parser.InlineParserFactory;
import me.mattstudios.msg.commonmark.parser.block.BlockContinue;
import me.mattstudios.msg.commonmark.parser.block.BlockParser;
import me.mattstudios.msg.commonmark.parser.block.BlockParserFactory;
import me.mattstudios.msg.commonmark.parser.block.BlockStart;
import me.mattstudios.msg.commonmark.parser.block.MatchedBlockParser;
import me.mattstudios.msg.commonmark.parser.block.ParserState;
import me.mattstudios.msg.commonmark.parser.delimiter.DelimiterProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentParser implements ParserState {

    private static final Set<Class<? extends Block>> CORE_FACTORY_TYPES = new LinkedHashSet<>(Arrays.asList(
            FencedCodeBlock.class,
            HtmlBlock.class,
            ThematicBreak.class,
            ListBlock.class,
            IndentedCodeBlock.class
    ));

    private static final Map<Class<? extends Block>, BlockParserFactory> NODES_TO_CORE_FACTORIES;

    static {
        Map<Class<? extends Block>, BlockParserFactory> map = new HashMap<>();
        map.put(FencedCodeBlock.class, new FencedCodeBlockParser.Factory());
        map.put(HtmlBlock.class, new HtmlBlockParser.Factory());
        map.put(ThematicBreak.class, new ThematicBreakParser.Factory());
        map.put(ListBlock.class, new ListBlockParser.Factory());
        map.put(IndentedCodeBlock.class, new IndentedCodeBlockParser.Factory());
        NODES_TO_CORE_FACTORIES = Collections.unmodifiableMap(map);
    }


    private CharSequence line;

    /**
     * Line index (0-based)
     */
    private int lineIndex = -1;

    /**
     * current index (offset) in input line (0-based)
     */
    private int index = 0;

    /**
     * current column of input line (tab causes column to go to next 4-space tab stop) (0-based)
     */
    private int column = 0;

    /**
     * if the current column is within a tab character (partially consumed tab)
     */
    private boolean columnIsInTab;

    private int nextNonSpace = 0;
    private int nextNonSpaceColumn = 0;
    private int indent = 0;
    private boolean blank;

    private final List<BlockParserFactory> blockParserFactories;
    private final InlineParserFactory inlineParserFactory;
    private final List<DelimiterProcessor> delimiterProcessors;
    private final List<TriggerProcessor> triggerProcessors;
    private final IncludeSourceSpans includeSourceSpans;
    private final DocumentBlockParser documentBlockParser;
    private final Map<String, LinkReferenceDefinition> definitions = new LinkedHashMap<>();

    private final List<OpenBlockParser> openBlockParsers = new ArrayList<>();
    private final List<BlockParser> allBlockParsers = new ArrayList<>();

    public DocumentParser(
            final List<BlockParserFactory> blockParserFactories,
            final InlineParserFactory inlineParserFactory,
            final List<DelimiterProcessor> delimiterProcessors,
            final List<TriggerProcessor> triggerProcessors,
            final IncludeSourceSpans includeSourceSpans
    ) {
        this.blockParserFactories = blockParserFactories;
        this.inlineParserFactory = inlineParserFactory;
        this.delimiterProcessors = delimiterProcessors;
        this.triggerProcessors = triggerProcessors;
        this.includeSourceSpans = includeSourceSpans;

        this.documentBlockParser = new DocumentBlockParser();
        activateBlockParser(new OpenBlockParser(documentBlockParser, 0));
    }

    public static Set<Class<? extends Block>> getDefaultBlockParserTypes() {
        return CORE_FACTORY_TYPES;
    }

    public static List<BlockParserFactory> calculateBlockParserFactories(List<BlockParserFactory> customBlockParserFactories, Set<Class<? extends Block>> enabledBlockTypes) {
        // By having the custom factories come first, extensions are able to change behavior of core syntax.
        List<BlockParserFactory> list = new ArrayList<>(customBlockParserFactories);
        for (Class<? extends Block> blockType : enabledBlockTypes) {
            list.add(NODES_TO_CORE_FACTORIES.get(blockType));
        }
        return list;
    }

    /**
     * The main parsing function. Returns a parsed document AST.
     */
    public Document parse(String input) {
        int lineStart = 0;

        if (input.length() > 0) {
            String line = input.substring(lineStart);
            incorporateLine(line);
        }

        return finalizeAndProcess();
    }

    public Document parse(Reader input) throws IOException {
        BufferedReader bufferedReader;
        if (input instanceof BufferedReader) {
            bufferedReader = (BufferedReader) input;
        } else {
            bufferedReader = new BufferedReader(input);
        }

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            incorporateLine(line);
        }

        return finalizeAndProcess();
    }

    @Override
    public CharSequence getLine() {
        return line;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getNextNonSpaceIndex() {
        return nextNonSpace;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getIndent() {
        return indent;
    }

    @Override
    public boolean isBlank() {
        return blank;
    }

    @Override
    public BlockParser getActiveBlockParser() {
        return openBlockParsers.get(openBlockParsers.size() - 1).blockParser;
    }

    /**
     * Analyze a line of text and update the document appropriately. We parse markdown text by calling this on each
     * line of input, then finalizing the document.
     */
    private void incorporateLine(CharSequence ln) {
        lineIndex++;
        line = Parsing.prepareLine(ln);
        index = 0;
        column = 0;
        columnIsInTab = false;

        // For each containing block, try to parse the associated line start.
        // The document will always match, so we can skip the first block parser and start at 1 matches
        int matches = 1;
        for (int i = 1; i < openBlockParsers.size(); i++) {
            OpenBlockParser openBlockParser = openBlockParsers.get(i);
            BlockParser blockParser = openBlockParser.blockParser;
            findNext();

            BlockContinue result = blockParser.tryContinue(this);
            if (result instanceof BlockContinueImpl) {
                BlockContinueImpl blockContinue = (BlockContinueImpl) result;
                openBlockParser.sourceIndex = getIndex();
                if (blockContinue.isFinalize()) {
                    addSourceSpans();
                    closeBlockParsers(openBlockParsers.size() - i);
                    return;
                } else {
                    if (blockContinue.getNewIndex() != -1) {
                        setNewIndex(blockContinue.getNewIndex());
                    } else if (blockContinue.getNewColumn() != -1) {
                        setNewColumn(blockContinue.getNewColumn());
                    }
                    matches++;
                }
            } else {
                break;
            }
        }

        int unmatchedBlocks = openBlockParsers.size() - matches;
        BlockParser blockParser = openBlockParsers.get(matches - 1).blockParser;
        boolean startedNewBlock = false;

        int lastIndex = index;

        // Unless last matched container is a code block, try new container starts,
        // adding children to the last matched container:
        boolean tryBlockStarts = blockParser.getBlock() instanceof Paragraph || blockParser.isContainer();
        while (tryBlockStarts) {
            lastIndex = index;
            findNext();

            // this is a little performance optimization:
            if (isBlank() || (indent < Parsing.CODE_BLOCK_INDENT && Parsing.isLetter(line, nextNonSpace))) {
                setNewIndex(nextNonSpace);
                break;
            }

            BlockStartImpl blockStart = findBlockStart(blockParser);
            if (blockStart == null) {
                setNewIndex(nextNonSpace);
                break;
            }

            startedNewBlock = true;
            int sourceIndex = getIndex();

            // We're starting a new block. If we have any previous blocks that need to be closed, we need to do it now.
            if (unmatchedBlocks > 0) {
                closeBlockParsers(unmatchedBlocks);
                unmatchedBlocks = 0;
            }

            if (blockStart.getNewIndex() != -1) {
                setNewIndex(blockStart.getNewIndex());
            } else if (blockStart.getNewColumn() != -1) {
                setNewColumn(blockStart.getNewColumn());
            }

            List<SourceSpan> replacedSourceSpans = null;
            if (blockStart.isReplaceActiveBlockParser()) {
                Block replacedBlock = prepareActiveBlockParserForReplacement();
                replacedSourceSpans = replacedBlock.getSourceSpans();
            }

            for (BlockParser newBlockParser : blockStart.getBlockParsers()) {
                addChild(new OpenBlockParser(newBlockParser, sourceIndex));
                if (replacedSourceSpans != null) {
                    newBlockParser.getBlock().setSourceSpans(replacedSourceSpans);
                }
                blockParser = newBlockParser;
                tryBlockStarts = newBlockParser.isContainer();
            }
        }

        // What remains at the offset is a text line. Add the text to the
        // appropriate block.

        // First check for a lazy paragraph continuation:
        if (!startedNewBlock && !isBlank() &&
                getActiveBlockParser().canHaveLazyContinuationLines()) {
            openBlockParsers.get(openBlockParsers.size() - 1).sourceIndex = lastIndex;
            // lazy paragraph continuation
            addLine();

        } else {

            // finalize any blocks not matched
            if (unmatchedBlocks > 0) {
                closeBlockParsers(unmatchedBlocks);
            }

            if (!blockParser.isContainer()) {
                addLine();
            } else if (!isBlank()) {
                // create paragraph container for line
                ParagraphParser paragraphParser = new ParagraphParser();
                addChild(new OpenBlockParser(paragraphParser, lastIndex));
                addLine();
            } else {
                // This can happen for a list item like this:
                // ```
                // *
                // list item
                // ```
                //
                // The first line does not start a paragraph yet, but we still want to record source positions.
                addSourceSpans();
            }
        }
    }

    private void findNext() {
        int i = index;
        int cols = column;

        blank = true;
        int length = line.length();
        while (i < length) {
            char c = line.charAt(i);
            blank = false;
            break;
        }

        nextNonSpace = i;
        nextNonSpaceColumn = cols;
        indent = nextNonSpaceColumn - column;
    }

    private void setNewIndex(int newIndex) {
        if (newIndex >= nextNonSpace) {
            // We can start from here, no need to calculate tab stops again
            index = nextNonSpace;
            column = nextNonSpaceColumn;
        }
        int length = line.length();
        while (index < newIndex && index != length) {
            advance();
        }
        // If we're going to an index as opposed to a column, we're never within a tab
        columnIsInTab = false;
    }

    private void setNewColumn(int newColumn) {
        if (newColumn >= nextNonSpaceColumn) {
            // We can start from here, no need to calculate tab stops again
            index = nextNonSpace;
            column = nextNonSpaceColumn;
        }
        int length = line.length();
        while (column < newColumn && index != length) {
            advance();
        }
        if (column > newColumn) {
            // Last character was a tab and we overshot our target
            index--;
            column = newColumn;
            columnIsInTab = true;
        } else {
            columnIsInTab = false;
        }
    }

    private void advance() {
        char c = line.charAt(index);
        if (c == '\t') {
            index++;
            column += Parsing.columnsToNextTabStop(column);
        } else {
            index++;
            column++;
        }
    }

    /**
     * Add line content to the active block parser. We assume it can accept lines -- that check should be done before
     * calling this.
     */
    private void addLine() {
        CharSequence content;
        if (columnIsInTab) {
            // Our column is in a partially consumed tab. Expand the remaining columns (to the next tab stop) to spaces.
            int afterTab = index + 1;
            CharSequence rest = line.subSequence(afterTab, line.length());
            int spaces = Parsing.columnsToNextTabStop(column);
            StringBuilder sb = new StringBuilder(spaces + rest.length());
            for (int i = 0; i < spaces; i++) {
                sb.append(' ');
            }
            sb.append(rest);
            content = sb.toString();
        } else if (index == 0) {
            content = line;
        } else {
            content = line.subSequence(index, line.length());
        }
        getActiveBlockParser().addLine(content);
        addSourceSpans();
    }

    private void addSourceSpans() {
        if (includeSourceSpans != IncludeSourceSpans.NONE) {
            // Don't add source spans for Document itself (it would get the whole source text)
            for (int i = 1; i < openBlockParsers.size(); i++) {
                OpenBlockParser openBlockParser = openBlockParsers.get(i);
                int blockIndex = openBlockParser.sourceIndex;
                int length = line.length() - blockIndex;
                if (length != 0) {
                    openBlockParser.blockParser.addSourceSpan(SourceSpan.of(lineIndex, blockIndex, length));
                }
            }
        }
    }

    private BlockStartImpl findBlockStart(BlockParser blockParser) {
        MatchedBlockParser matchedBlockParser = new MatchedBlockParserImpl(blockParser);
        for (BlockParserFactory blockParserFactory : blockParserFactories) {
            BlockStart result = blockParserFactory.tryStart(this, matchedBlockParser);
            if (result instanceof BlockStartImpl) {
                return (BlockStartImpl) result;
            }
        }
        return null;
    }

    /**
     * Finalize a block. Close it and do any necessary postprocessing, e.g. setting the content of blocks and
     * collecting link reference definitions from paragraphs.
     */
    private void finalize(BlockParser blockParser) {
        if (blockParser instanceof ParagraphParser) {
            addDefinitionsFrom((ParagraphParser) blockParser);
        }

        blockParser.closeBlock();
    }

    private void addDefinitionsFrom(ParagraphParser paragraphParser) {
        for (LinkReferenceDefinition definition : paragraphParser.getDefinitions()) {
            // Add nodes into document before paragraph.
            paragraphParser.getBlock().insertBefore(definition);

            String label = definition.getLabel();
            // spec: When there are multiple matching link reference definitions, the first is used
            if (!definitions.containsKey(label)) {
                definitions.put(label, definition);
            }
        }
    }

    /**
     * Walk through a block & children recursively, parsing string content into inline content where appropriate.
     */
    private void processInlines() {
        InlineParserContextImpl context = new InlineParserContextImpl(delimiterProcessors, triggerProcessors, definitions);
        InlineParser inlineParser = inlineParserFactory.create(context);

        for (BlockParser blockParser : allBlockParsers) {
            blockParser.parseInlines(inlineParser);
        }
    }

    /**
     * Add block of type tag as a child of the tip. If the tip can't accept children, close and finalize it and try
     * its parent, and so on until we find a block that can accept children.
     */
    private void addChild(OpenBlockParser openBlockParser) {
        while (!getActiveBlockParser().canContain(openBlockParser.blockParser.getBlock())) {
            closeBlockParsers(1);
        }

        getActiveBlockParser().getBlock().appendChild(openBlockParser.blockParser.getBlock());
        activateBlockParser(openBlockParser);
    }

    private void activateBlockParser(OpenBlockParser openBlockParser) {
        openBlockParsers.add(openBlockParser);
    }

    private OpenBlockParser deactivateBlockParser() {
        return openBlockParsers.remove(openBlockParsers.size() - 1);
    }

    private Block prepareActiveBlockParserForReplacement() {
        // Note that we don't want to parse inlines, as it's getting replaced.
        BlockParser old = deactivateBlockParser().blockParser;

        if (old instanceof ParagraphParser) {
            ParagraphParser paragraphParser = (ParagraphParser) old;
            // Collect any link reference definitions. Note that replacing the active block parser is done after a
            // block parser got the current paragraph content using MatchedBlockParser#getContentString. In case the
            // paragraph started with link reference definitions, we parse and strip them before the block parser gets
            // the content. We want to keep them.
            // If no replacement happens, we collect the definitions as part of finalizing paragraph blocks.
            addDefinitionsFrom(paragraphParser);
        }

        // Do this so that source positions are calculated, which we will carry over to the replacing block.
        old.closeBlock();
        old.getBlock().unlink();
        return old.getBlock();
    }

    private Document finalizeAndProcess() {
        closeBlockParsers(openBlockParsers.size());
        processInlines();
        return documentBlockParser.getBlock();
    }

    private void closeBlockParsers(int count) {
        for (int i = 0; i < count; i++) {
            BlockParser blockParser = deactivateBlockParser().blockParser;
            finalize(blockParser);
            // Remember for inline parsing. Note that a lot of blocks don't need inline parsing. We could have a
            // separate interface (e.g. BlockParserWithInlines) so that we only have to remember those that actually
            // have inlines to parse.
            allBlockParsers.add(blockParser);
        }
    }

    private static class MatchedBlockParserImpl implements MatchedBlockParser {

        private final BlockParser matchedBlockParser;

        public MatchedBlockParserImpl(BlockParser matchedBlockParser) {
            this.matchedBlockParser = matchedBlockParser;
        }

        @Override
        public BlockParser getMatchedBlockParser() {
            return matchedBlockParser;
        }

        @Override
        public List<CharSequence> getParagraphLines() {
            if (matchedBlockParser instanceof ParagraphParser) {
                ParagraphParser paragraphParser = (ParagraphParser) matchedBlockParser;
                return Collections.unmodifiableList(paragraphParser.getParagraphLines());
            }
            return Collections.emptyList();
        }
    }

    private static class OpenBlockParser {
        private final BlockParser blockParser;
        private int sourceIndex;

        OpenBlockParser(BlockParser blockParser, int sourceIndex) {
            this.blockParser = blockParser;
            this.sourceIndex = sourceIndex;
        }
    }
}
