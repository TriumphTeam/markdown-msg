package me.mattstudios.mfmsg.base;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Lexer {

    private Lexer() {}

    //private static final Pattern stylePattern = Pattern.compile("(?<HEX><#.+?>)|(?<ACTION>(?<!\\\\)\\[.+?(?<!\\\\)](?<!\\\\)\\(.+?(?<!\\\\)\\))|(?<BI>(?<!\\\\)\\*+.+?(?<!\\\\)\\*+)|(?<STRIKE>(?<!\\\\)~+.+?~+)|(?<ESCAPED>\\\\[*~\\[\\]()])");
    private static final Set<TokenType> tokenTypes = Arrays.stream(TokenType.possibleValues())
                                                            .filter(tokenType -> tokenType != TokenType.TEXT)
                                                            .filter(tokenType -> tokenType != TokenType.ESCAPE)
                                                            .collect(Collectors.toSet());

    public static List<Token> lex(final String input) {
        final List<Token> lexed = new ArrayList<>();

        final char[] chars = input.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            final char currentChar = chars[i];

            boolean matched = false;

            // Looks for token matches
            for (final TokenType tokenType : tokenTypes) {
                if (currentChar != tokenType.getChar()) continue;
                matched = true;

                // Checks if token is escaped or not .. needs work
                if (isEscaped(chars, i)) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.append(currentChar);
                    continue;
                }

                // Turns the builder into a TEXT since nothing else was detected
                if (stringBuilder.length() != 0) {
                    lexed.add(new Token(TokenType.TEXT, stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }

                lexed.add(new Token(tokenType, String.valueOf(currentChar)));
                break;
            }

            if (!matched) stringBuilder.append(currentChar);
        }
        if (stringBuilder.length() != 0)
        lexed.add(new Token(TokenType.TEXT, stringBuilder.toString()));

        return lexed;
    }

    private static boolean isEscaped(final char[] chars, final int i) {
        // Checks for \*
        if (i >= 1) {
            return chars[i - 1] == TokenType.ESCAPE.getChar();
        }

        // TODO, check for \\*, won't work
        return i != 0 && chars[i - 1] == TokenType.ESCAPE.getChar() && chars[i - 2] != TokenType.ESCAPE.getChar();
    }


    public static void main(String[] args) {
        Parser parser = new Parser(lex("*lol*"));
        parser.parseText();
        System.out.println(Grammar.BOLD.partialMatches(parser.getStack()) + " <- Grammar result");
    }

}
