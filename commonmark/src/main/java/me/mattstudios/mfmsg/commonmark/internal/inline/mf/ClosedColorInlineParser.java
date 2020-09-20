package me.mattstudios.mfmsg.commonmark.internal.inline.mf;

import com.google.common.primitives.Floats;
import me.mattstudios.mfmsg.commonmark.internal.inline.InlineContentParser;
import me.mattstudios.mfmsg.commonmark.internal.inline.InlineParserState;
import me.mattstudios.mfmsg.commonmark.internal.inline.ParsedInline;
import me.mattstudios.mfmsg.commonmark.internal.inline.Position;
import me.mattstudios.mfmsg.commonmark.internal.inline.Scanner;
import me.mattstudios.mfmsg.commonmark.internal.util.AsciiMatcher;
import me.mattstudios.mfmsg.commonmark.node.mf.Color;
import me.mattstudios.mfmsg.commonmark.node.mf.Gradient;
import me.mattstudios.mfmsg.commonmark.node.mf.Rainbow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Attempts to parse a `&` format
 */
@SuppressWarnings("UnstableApiUsage")
public class ClosedColorInlineParser implements InlineContentParser {

    private static final char CLOSE_CHARACTER = '>';
    private static final char SEPARATOR_CHARACTER = ':';
    private static final char HEX_CHARACTER = '#';

    @NotNull
    private static final AsciiMatcher HEX = AsciiMatcher.builder().range('0', '9').range('A', 'F').range('a', 'f').build();

    @Override
    public ParsedInline tryParse(@NotNull final InlineParserState inlineParserState) {
        final Scanner scanner = inlineParserState.scanner();

        // Skip `<`
        scanner.next();

        final char c = scanner.peek();
        if (c == HEX_CHARACTER) {
            // Handling HEX
            final Position start = scanner.position();

            for (int i = 0; i <= 6; i++) {
                // Goes for the next and skips #
                scanner.next();
                // Checks if the character is valid hex, need to be either 3 or 6 characters
                final char hexChar = scanner.peek();
                if (!HEX.matches(hexChar)) {
                    if (i != 3 && i != 6) break;

                    // If not closed with '>' breaks
                    if (hexChar != CLOSE_CHARACTER) break;

                    return color(scanner, start);
                }
            }

            return ParsedInline.none();
        }

        if (c == 'r') {
            // Handling rainbow
            scanner.next();
            final char separatorChar = scanner.peek();
            if (separatorChar != SEPARATOR_CHARACTER) {
                // Handles "<r>"
                if (separatorChar == CLOSE_CHARACTER) {
                    return rainbow(scanner, 1f, 1f);
                }

                return ParsedInline.none();
            }

            final List<Float> values = new ArrayList<>();
            final StringBuilder valueBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                scanner.next();
                final char currentChar = scanner.peek();
                if (values.size() >= 2) break;

                if (currentChar == SEPARATOR_CHARACTER && valueBuilder.length() != 0) {
                    appendValue(values, valueBuilder);
                    continue;
                }

                if (currentChar == CLOSE_CHARACTER) {
                    if (valueBuilder.length() != 0) appendValue(values, valueBuilder);
                    break;
                }

                if (Character.isDigit(currentChar) || currentChar == '.') {
                    valueBuilder.append(currentChar);
                    continue;
                }

                break;
            }

            if (values.isEmpty()) return ParsedInline.none();
            if (values.size() == 1) return rainbow(scanner, values.get(0), 1f);
            if (values.size() == 2) return rainbow(scanner, values.get(0), values.get(1));
        }

        if (c == 'g') {
            // Handling gradient
            scanner.next();

            // If no separator is found return none
            if (scanner.peek() != SEPARATOR_CHARACTER) {
                return ParsedInline.none();
            }

            final List<String> hexes = new ArrayList<>();
            final StringBuilder hexBuilder = new StringBuilder();

            // Loops through all the possible hex codes
            while (scanner.hasNext()) {
                scanner.next();
                char currentChar = scanner.peek();

                // Sets a maximum of 5 hexes, if more return none
                if (hexes.size() >= 5) {
                    return ParsedInline.none();
                }

                // If doesn't start with '#' returns none
                if (currentChar != HEX_CHARACTER) {
                    return ParsedInline.none();
                }

                // Appends '#'
                hexBuilder.append(currentChar);

                // Loops through the next characters searching for hexes
                for (int i = 0; i <= 6; i++) {
                    scanner.next();
                    currentChar = scanner.peek();

                    // If not a valid hex character
                    if (!HEX.matches(currentChar)) {
                        // If not exactly 3 or 6 characters, return none
                        if (i != 3 && i != 6) {
                            return ParsedInline.none();
                        }

                        // Appends current hex and resets builder
                        hexes.add(hexBuilder.toString());
                        hexBuilder.setLength(0);

                        // Checks for close character
                        if (currentChar == CLOSE_CHARACTER) {
                            return gradient(scanner, hexes);
                        }

                        break;
                    }

                    // Appends hex character
                    hexBuilder.append(currentChar);
                }
            }

        }

        return ParsedInline.none();
    }

    private void appendValue(@NotNull final List<Float> values, @NotNull final StringBuilder valueBuilder) {
        final Float floatValue = Floats.tryParse(valueBuilder.toString());
        if (floatValue == null) return;
        values.add(floatValue);
        valueBuilder.setLength(0);
    }

    private ParsedInline color(@NotNull final Scanner scanner, @NotNull final Position start) {
        final String hex = scanner.textBetween(start, scanner.position()).toString();
        // Skips the closing '>'
        scanner.next();
        return ParsedInline.of(new Color(hex), scanner.position());
    }

    private ParsedInline rainbow(@NotNull final Scanner scanner, final float saturation, final float brightness) {
        // Skips the closing '>'
        scanner.next();
        return ParsedInline.of(new Rainbow(saturation, brightness), scanner.position());
    }

    private ParsedInline gradient(@NotNull final Scanner scanner, @NotNull final List<String> hexes) {
        // Skips the closing '>'
        scanner.next();
        return ParsedInline.of(new Gradient(hexes), scanner.position());
    }

}
