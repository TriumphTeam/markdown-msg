package me.mattstudios.mfmsg.commonmark.internal.inline.mf;

import me.mattstudios.mfmsg.commonmark.internal.inline.InlineContentParser;
import me.mattstudios.mfmsg.commonmark.internal.inline.InlineParserState;
import me.mattstudios.mfmsg.commonmark.internal.inline.ParsedInline;
import me.mattstudios.mfmsg.commonmark.internal.inline.Position;
import me.mattstudios.mfmsg.commonmark.internal.inline.Scanner;
import me.mattstudios.mfmsg.commonmark.internal.util.AsciiMatcher;
import me.mattstudios.mfmsg.commonmark.node.mf.Color;
import me.mattstudios.mfmsg.commonmark.node.mf.Reset;
import org.jetbrains.annotations.NotNull;

/**
 * Attempts to parse a `&` format
 */
public class ColorInlineParser implements InlineContentParser {

    @NotNull
    private static final AsciiMatcher HEX = AsciiMatcher.builder().range('0', '9').range('A', 'F').range('a', 'f').build();

    @Override
    public ParsedInline tryParse(@NotNull final InlineParserState inlineParserState) {
        final Scanner scanner = inlineParserState.scanner();
        // Skip `&`
        scanner.next();
        final Position start = scanner.position();

        final char c = scanner.peek();
        if (c == '#') {
            // Handling HEX
            int i = 0;
            while (scanner.hasNext()) {
                // Goes for the next and skips #
                scanner.next();
                // Checks if the character is valid hex, need to be either 3 or 6 characters
                if (!HEX.matches(scanner.peek()) || i > 6) {
                    if (i != 3 && i != 6) break;

                    return color(scanner, start);
                }

                i++;
            }

            return ParsedInline.none();
        }

        // Sets the scanner position to the character
        scanner.next();

        // Checks if matches legacy code
        if (HEX.matches(c)) {
            return color(scanner, start, true);
        }

        // Checks for reset character
        if (c == 'r') {
            return ParsedInline.of(new Reset(), scanner.position());
        }


        return ParsedInline.none();
    }

    private ParsedInline color(@NotNull final Scanner scanner, @NotNull final Position start) {
        return color(scanner, start, false);
    }

    private ParsedInline color(@NotNull final Scanner scanner, @NotNull final Position start, final boolean legacy) {
        final String hex = scanner.textBetween(start, scanner.position()).toString();
        return ParsedInline.of(new Color(hex, legacy), scanner.position());
    }

}
