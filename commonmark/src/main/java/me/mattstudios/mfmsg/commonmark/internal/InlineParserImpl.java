package me.mattstudios.mfmsg.commonmark.internal;

import me.mattstudios.mfmsg.commonmark.internal.inline.AsteriskDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.internal.inline.BackslashInlineParser;
import me.mattstudios.mfmsg.commonmark.internal.inline.InlineContentParser;
import me.mattstudios.mfmsg.commonmark.internal.inline.InlineParserState;
import me.mattstudios.mfmsg.commonmark.internal.inline.ParsedInline;
import me.mattstudios.mfmsg.commonmark.internal.inline.ParsedInlineImpl;
import me.mattstudios.mfmsg.commonmark.internal.inline.Position;
import me.mattstudios.mfmsg.commonmark.internal.inline.Scanner;
import me.mattstudios.mfmsg.commonmark.internal.inline.UnderscoreDelimiterProcessor;
import me.mattstudios.mfmsg.commonmark.internal.inline.mf.ActionScanner;
import me.mattstudios.mfmsg.commonmark.internal.inline.mf.ClosedColorInlineParser;
import me.mattstudios.mfmsg.commonmark.internal.inline.mf.ColorInlineParser;
import me.mattstudios.mfmsg.commonmark.internal.util.Parsing;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Text;
import me.mattstudios.mfmsg.commonmark.node.mf.Action;
import me.mattstudios.mfmsg.commonmark.parser.InlineParser;
import me.mattstudios.mfmsg.commonmark.parser.InlineParserContext;
import me.mattstudios.mfmsg.commonmark.parser.delimiter.DelimiterProcessor;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class InlineParserImpl implements InlineParser, InlineParserState {

    private static final String ASCII_PUNCTUATION = "!\"#\\$%&'\\(\\)\\*\\+,\\-\\./:;<=>\\?@\\[\\\\\\]\\^_`\\{\\|\\}~";
    private static final Pattern PUNCTUATION = Pattern
            .compile("^[" + ASCII_PUNCTUATION + "\\p{Pc}\\p{Pd}\\p{Pe}\\p{Pf}\\p{Pi}\\p{Po}\\p{Ps}]");

    private static final Pattern UNICODE_WHITESPACE_CHAR = Pattern.compile("^[\\p{Zs}\t\r\n\f]");

    private final BitSet specialCharacters;
    private final BitSet delimiterCharacters;
    private final Map<Character, DelimiterProcessor> delimiterProcessors;
    private final Map<Character, InlineContentParser> inlineParsers;

    private Scanner scanner;
    private int trailingSpaces;

    /**
     * Top delimiter (emphasis, strong emphasis or custom emphasis). (Brackets are on a separate stack, different
     * from the algorithm described in the spec.)
     */
    private Delimiter lastDelimiter;

    /**
     * Top opening bracket (<code>[</code> or <code>![)</code>).
     */
    private Bracket lastBracket;

    public InlineParserImpl(InlineParserContext inlineParserContext) {
        this.delimiterProcessors = calculateDelimiterProcessors(inlineParserContext.getCustomDelimiterProcessors());

        this.inlineParsers = new HashMap<>();
        this.inlineParsers.put('\\', new BackslashInlineParser());
        this.inlineParsers.put('&', new ColorInlineParser());
        this.inlineParsers.put('<', new ClosedColorInlineParser());

        this.delimiterCharacters = calculateDelimiterCharacters(this.delimiterProcessors.keySet());
        this.specialCharacters = calculateSpecialCharacters(delimiterCharacters, inlineParsers.keySet());
    }

    public static BitSet calculateDelimiterCharacters(Set<Character> characters) {
        BitSet bitSet = new BitSet();
        for (Character character : characters) {
            bitSet.set(character);
        }
        return bitSet;
    }

    public static BitSet calculateSpecialCharacters(BitSet delimiterCharacters, Set<Character> characters) {
        BitSet bitSet = new BitSet();
        bitSet.or(delimiterCharacters);
        for (Character c : characters) {
            bitSet.set(c);
        }
        bitSet.set('[');
        bitSet.set(']');
        return bitSet;
    }

    public static Map<Character, DelimiterProcessor> calculateDelimiterProcessors(List<DelimiterProcessor> delimiterProcessors) {
        Map<Character, DelimiterProcessor> map = new HashMap<>();
        addDelimiterProcessors(Arrays.asList(new AsteriskDelimiterProcessor(), new UnderscoreDelimiterProcessor()), map);
        addDelimiterProcessors(delimiterProcessors, map);
        return map;
    }

    @Override
    public Scanner scanner() {
        return scanner;
    }

    private static void addDelimiterProcessors(Iterable<DelimiterProcessor> delimiterProcessors, Map<Character, DelimiterProcessor> map) {
        for (DelimiterProcessor delimiterProcessor : delimiterProcessors) {
            char opening = delimiterProcessor.getOpeningCharacter();
            char closing = delimiterProcessor.getClosingCharacter();
            if (opening == closing) {
                DelimiterProcessor old = map.get(opening);
                if (old != null && old.getOpeningCharacter() == old.getClosingCharacter()) {
                    StaggeredDelimiterProcessor s;
                    if (old instanceof StaggeredDelimiterProcessor) {
                        s = (StaggeredDelimiterProcessor) old;
                    } else {
                        s = new StaggeredDelimiterProcessor(opening);
                        s.add(old);
                    }
                    s.add(delimiterProcessor);
                    map.put(opening, s);
                } else {
                    addDelimiterProcessorForChar(opening, delimiterProcessor, map);
                }
            } else {
                addDelimiterProcessorForChar(opening, delimiterProcessor, map);
                addDelimiterProcessorForChar(closing, delimiterProcessor, map);
            }
        }
    }

    private static void addDelimiterProcessorForChar(char delimiterChar, DelimiterProcessor toAdd, Map<Character, DelimiterProcessor> delimiterProcessors) {
        DelimiterProcessor existing = delimiterProcessors.put(delimiterChar, toAdd);
        if (existing != null) {
            throw new IllegalArgumentException("Delimiter processor conflict with delimiter char '" + delimiterChar + "'");
        }
    }

    /**
     * Parse content in block into inline children, appending them to the block node.
     */
    @Override
    public void parse(List<CharSequence> lines, Node block) {
        reset(lines);

        while (true) {
            Node node = parseInline();
            if (node != null) {
                block.appendChild(node);
            } else {
                break;
            }
        }

        processDelimiters(null);
        mergeChildTextNodes(block);
    }

    void reset(List<CharSequence> lines) {
        this.scanner = Scanner.of(lines);
        this.trailingSpaces = 0;
        this.lastDelimiter = null;
        this.lastBracket = null;
    }

    private Text text(String text) {
        return new Text(text);
    }

    /**
     * Parse the next inline element in subject, advancing our position.
     * On success, return the new inline node.
     * On failure, return null.
     */
    private Node parseInline() {
        char c = scanner.peek();
        if (c == Scanner.END) {
            return null;
        }

        Position position = scanner.position();
        InlineContentParser inlineParser = this.inlineParsers.get(c);
        if (inlineParser != null) {
            ParsedInline parsedInline = inlineParser.tryParse(this);
            if (parsedInline instanceof ParsedInlineImpl) {
                ParsedInlineImpl parsedInlineImpl = (ParsedInlineImpl) parsedInline;
                scanner.setPosition(parsedInlineImpl.getPosition());
                return parsedInlineImpl.getNode();
            } else {
                // Reset position
                scanner.setPosition(position);
            }
        }

        switch (c) {
            case '[':
                return parseOpenBracket();
            case ']':
                return parseCloseBracket();
        }

        boolean isDelimiter = delimiterCharacters.get(c);
        if (isDelimiter) {
            DelimiterProcessor delimiterProcessor = delimiterProcessors.get(c);
            Node delimiterNode = parseDelimiters(delimiterProcessor, c);
            if (delimiterNode != null) {
                return delimiterNode;
            }
        }

        // If we get here, even for a special/delimiter character, we will just treat it as text.
        return parseText();
    }

    /**
     * Attempt to parse delimiters like emphasis, strong emphasis or custom delimiters.
     */
    private Node parseDelimiters(DelimiterProcessor delimiterProcessor, char delimiterChar) {
        DelimiterData res = scanDelimiters(delimiterProcessor, delimiterChar);
        if (res == null) {
            return null;
        }

        Text node = res.text;

        // Add entry to stack for this opener
        lastDelimiter = new Delimiter(node, delimiterChar, res.canOpen, res.canClose, lastDelimiter);
        lastDelimiter.length = res.count;
        lastDelimiter.originalLength = res.count;
        if (lastDelimiter.previous != null) {
            lastDelimiter.previous.next = lastDelimiter;
        }

        return node;
    }

    /**
     * Add open bracket to delimiter stack and add a text node to block's children.
     */
    private Node parseOpenBracket() {
        scanner.next();
        Position start = scanner.position();

        Text node = text("[");

        // Add entry to stack for this opener
        addBracket(Bracket.link(node, start, lastBracket, lastDelimiter));

        return node;
    }

    /**
     * If next character is [, and ! delimiter to delimiter stack and add a text node to block's children.
     * Otherwise just add a text node.
     */
    private Node parseBang() {
        scanner.next();
        if (scanner.next('[')) {
            Text node = text("![");

            // Add entry to stack for this opener
            addBracket(Bracket.image(node, scanner.position(), lastBracket, lastDelimiter));
            return node;
        } else {
            return text("!");
        }
    }

    /**
     * Try to match close bracket against an opening in the delimiter stack. Return either a link or image, or a
     * plain [ character. If there is a matching delimiter, remove it from the delimiter stack.
     */
    private Node parseCloseBracket() {
        Position beforeClose = scanner.position();
        scanner.next();
        Position afterClose = scanner.position();

        // Get previous `[`
        Bracket opener = lastBracket;
        if (opener == null) {
            // No matching opener, just return a literal.
            return text("]");
        }

        if (!opener.allowed) {
            // Matching opener but it's not allowed, just return a literal.
            removeLastBracket();
            return text("]");
        }

        // Check to see if we have a link/image
        Map<String, String> actions = null;

        // Maybe a inline link like `[foo](hover: Test)`
        if (scanner.next('(')) {
            scanner.whitespace();

            actions = ActionScanner.scanAction(scanner);

            if (actions.isEmpty()) {
                scanner.setPosition(afterClose);
            } else {

                if (!scanner.next(')')) {
                    // Don't have a closing `)`, so it's not a destination and title -> reset.
                    // Note that something like `[foo](` could be valid, `(` will just be text.
                    scanner.setPosition(afterClose);
                    actions = null;
                }
            }
        }

        if (actions != null && !actions.isEmpty()) {
            // If we got here, open is a potential opener
            Node action = new Action(actions);

            Node node = opener.node.getNext();
            while (node != null) {
                Node next = node.getNext();
                action.appendChild(node);
                node = next;
            }

            // Process delimiters such as emphasis inside link/image
            processDelimiters(opener.previousDelimiter);
            mergeChildTextNodes(action);
            // We don't need the corresponding text node anymore, we turned it into a link/image node
            opener.node.unlink();
            removeLastBracket();

            // Links within links are not allowed. We found this link, so there can be no other link around it.
            if (!opener.image) {
                Bracket bracket = lastBracket;
                while (bracket != null) {
                    if (!bracket.image) {
                        // Disallow link opener. It will still get matched, but will not result in a link.
                        bracket.allowed = false;
                    }
                    bracket = bracket.previous;
                }
            }

            return action;

        } else {
            // No link or image, parse just the bracket as text and continue
            removeLastBracket();

            scanner.setPosition(afterClose);
            return text("]");
        }
    }

    private void addBracket(Bracket bracket) {
        if (lastBracket != null) {
            lastBracket.bracketAfter = true;
        }
        lastBracket = bracket;
    }

    private void removeLastBracket() {
        lastBracket = lastBracket.previous;
    }

    /**
     * Parse the next character as plain text, and possibly more if the following characters are non-special.
     */
    private Node parseText() {
        Position start = scanner.position();
        scanner.next();
        while (scanner.hasNext()) {
            if (specialCharacters.get(scanner.peek())) {
                break;
            }
            scanner.next();
        }

        String text = scanner.textBetween(start, scanner.position()).toString();

        char c = scanner.peek();
        if (c == '\n') {
            // We parsed until the end of the line. Trim any trailing spaces and remember them (for hard line breaks).
            int end = Parsing.skipBackwards(' ', text, text.length() - 1, 0) + 1;
            trailingSpaces = text.length() - end;
            text = text.substring(0, end);
        } else if (c == Scanner.END) {
            // For the last line, both tabs and spaces are trimmed for some reason (checked with commonmark.js).
            int end = Parsing.skipSpaceTabBackwards(text, text.length() - 1, 0) + 1;
            text = text.substring(0, end);
        }

        return text(text);
    }

    /**
     * Scan a sequence of characters with code delimiterChar, and return information about the number of delimiters
     * and whether they are positioned such that they can open and/or close emphasis or strong emphasis.
     *
     * @return information about delimiter run, or {@code null}
     */
    private DelimiterData scanDelimiters(DelimiterProcessor delimiterProcessor, char delimiterChar) {
        Position start = scanner.position();

        int delimiterCount = scanner.matchMultiple(delimiterChar);

        if (delimiterCount < delimiterProcessor.getMinLength()) {
            scanner.setPosition(start);
            return null;
        }

        // Changed here to allow the syntax `He__llo__` and changed to allow `**. hello .**`
        boolean canOpen = delimiterChar == delimiterProcessor.getOpeningCharacter();
        boolean canClose = delimiterChar == delimiterProcessor.getClosingCharacter();

        String text = scanner.textBetween(start, scanner.position()).toString();
        return new DelimiterData(delimiterCount, canOpen, canClose, new Text(text));
    }

    private void processDelimiters(Delimiter stackBottom) {

        Map<Character, Delimiter> openersBottom = new HashMap<>();

        // find first closer above stackBottom:
        Delimiter closer = lastDelimiter;
        while (closer != null && closer.previous != stackBottom) {
            closer = closer.previous;
        }
        // move forward, looking for closers, and handling each
        while (closer != null) {
            char delimiterChar = closer.delimiterChar;

            DelimiterProcessor delimiterProcessor = delimiterProcessors.get(delimiterChar);
            if (!closer.canClose || delimiterProcessor == null) {
                closer = closer.next;
                continue;
            }

            char openingDelimiterChar = delimiterProcessor.getOpeningCharacter();

            // Found delimiter closer. Now look back for first matching opener.
            int useDelims = 0;
            boolean openerFound = false;
            boolean potentialOpenerFound = false;
            Delimiter opener = closer.previous;
            while (opener != null && opener != stackBottom && opener != openersBottom.get(delimiterChar)) {
                if (opener.canOpen && opener.delimiterChar == openingDelimiterChar) {
                    potentialOpenerFound = true;
                    useDelims = delimiterProcessor.getDelimiterUse(opener, closer);
                    if (useDelims > 0) {
                        openerFound = true;
                        break;
                    }
                }
                opener = opener.previous;
            }

            if (!openerFound) {
                if (!potentialOpenerFound) {
                    // Set lower bound for future searches for openers.
                    // Only do this when we didn't even have a potential
                    // opener (one that matches the character and can open).
                    // If an opener was rejected because of the number of
                    // delimiters (e.g. because of the "multiple of 3" rule),
                    // we want to consider it next time because the number
                    // of delimiters can change as we continue processing.
                    openersBottom.put(delimiterChar, closer.previous);
                    if (!closer.canOpen) {
                        // We can remove a closer that can't be an opener,
                        // once we've seen there's no matching opener:
                        removeDelimiterKeepNode(closer);
                    }
                }
                closer = closer.next;
                continue;
            }

            Text openerNode = opener.node;
            Text closerNode = closer.node;

            // Remove number of used delimiters from stack and inline nodes.
            opener.length -= useDelims;
            closer.length -= useDelims;
            openerNode.setLiteral(
                    openerNode.getLiteral().substring(0,
                                                      openerNode.getLiteral().length() - useDelims));
            closerNode.setLiteral(
                    closerNode.getLiteral().substring(0,
                                                      closerNode.getLiteral().length() - useDelims));

            removeDelimitersBetween(opener, closer);
            // The delimiter processor can re-parent the nodes between opener and closer,
            // so make sure they're contiguous already. Exclusive because we want to keep opener/closer themselves.
            mergeTextNodesBetweenExclusive(openerNode, closerNode);
            delimiterProcessor.process(openerNode, closerNode, useDelims);

            // No delimiter characters left to process, so we can remove delimiter and the now empty node.
            if (opener.length == 0) {
                removeDelimiterAndNode(opener);
            }

            if (closer.length == 0) {
                Delimiter next = closer.next;
                removeDelimiterAndNode(closer);
                closer = next;
            }
        }

        // remove all delimiters
        while (lastDelimiter != null && lastDelimiter != stackBottom) {
            removeDelimiterKeepNode(lastDelimiter);
        }
    }

    private void removeDelimitersBetween(Delimiter opener, Delimiter closer) {
        Delimiter delimiter = closer.previous;
        while (delimiter != null && delimiter != opener) {
            Delimiter previousDelimiter = delimiter.previous;
            removeDelimiterKeepNode(delimiter);
            delimiter = previousDelimiter;
        }
    }

    /**
     * Remove the delimiter and the corresponding text node. For used delimiters, e.g. `*` in `*foo*`.
     */
    private void removeDelimiterAndNode(Delimiter delim) {
        Text node = delim.node;
        node.unlink();
        removeDelimiter(delim);
    }

    /**
     * Remove the delimiter but keep the corresponding node as text. For unused delimiters such as `_` in `foo_bar`.
     */
    private void removeDelimiterKeepNode(Delimiter delim) {
        removeDelimiter(delim);
    }

    private void removeDelimiter(Delimiter delim) {
        if (delim.previous != null) {
            delim.previous.next = delim.next;
        }
        if (delim.next == null) {
            // top of stack
            lastDelimiter = delim.previous;
        } else {
            delim.next.previous = delim.previous;
        }
    }

    private void mergeTextNodesBetweenExclusive(Node fromNode, Node toNode) {
        // No nodes between them
        if (fromNode == toNode || fromNode.getNext() == toNode) {
            return;
        }

        mergeTextNodesInclusive(fromNode.getNext(), toNode.getPrevious());
    }

    private void mergeChildTextNodes(Node node) {
        // No children or just one child node, no need for merging
        if (node.getFirstChild() == node.getLastChild()) {
            return;
        }

        mergeTextNodesInclusive(node.getFirstChild(), node.getLastChild());
    }

    private void mergeTextNodesInclusive(Node fromNode, Node toNode) {
        Text first = null;
        Text last = null;
        int length = 0;

        Node node = fromNode;
        while (node != null) {
            if (node instanceof Text) {
                Text text = (Text) node;
                if (first == null) {
                    first = text;
                }
                length += text.getLiteral().length();
                last = text;
            } else {
                mergeIfNeeded(first, last, length);
                first = null;
                last = null;
                length = 0;
            }
            if (node == toNode) {
                break;
            }
            node = node.getNext();
        }

        mergeIfNeeded(first, last, length);
    }

    private void mergeIfNeeded(Text first, Text last, int textLength) {
        if (first != null && last != null && first != last) {
            StringBuilder sb = new StringBuilder(textLength);
            sb.append(first.getLiteral());
            Node node = first.getNext();
            Node stop = last.getNext();
            while (node != stop) {
                sb.append(((Text) node).getLiteral());
                Node unlink = node;
                node = node.getNext();
                unlink.unlink();
            }
            String literal = sb.toString();
            first.setLiteral(literal);
        }
    }

    private static class DelimiterData {

        final int count;
        final boolean canClose;
        final boolean canOpen;
        final Text text;

        DelimiterData(int count, boolean canOpen, boolean canClose, Text text) {
            this.count = count;
            this.canOpen = canOpen;
            this.canClose = canClose;
            this.text = text;
        }
    }
}
