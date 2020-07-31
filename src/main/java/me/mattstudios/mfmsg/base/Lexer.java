package me.mattstudios.mfmsg.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Lexer {

    private Lexer() {}

    private static final Pattern stylePattern = Pattern.compile("(?<HEX><#.+?>)|(?<ACTION>(?<!\\\\)\\[.+?(?<!\\\\)](?<!\\\\)\\(.+?(?<!\\\\)\\))|(?<BI>(?<!\\\\)\\*+.+?(?<!\\\\)\\*+)|(?<STRIKE>(?<!\\\\)~+.+?~+)|(?<ESCAPED>\\\\[*~\\[\\]()])");
    private static final List<TokenType> tokenTypes = Arrays.asList(TokenType.values()).parallelStream().filter(tokenType -> tokenType != TokenType.TEXT).collect(Collectors.toList());

    public static List<Token> lex(final String input) {
        final List<Token> lexed = new ArrayList<>();

        final char[] chars = input.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            final char currentChar = chars[i];

            if (currentChar == TokenType.ASTERISK.getChar()) {
                // Handles escaping
                if (i > 0 && chars[i - 1] == TokenType.ESCAPE.getChar()) {
                    stringBuilder.append(currentChar);
                    continue;
                }

                // Turns the builder into a TEXT since nothing else was detected
                if (stringBuilder.length() != 0) {
                    lexed.add(new Token(TokenType.TEXT, stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }

                lexed.add(new Token(TokenType.ASTERISK, String.valueOf(currentChar)));

                continue;
            }

            stringBuilder.append(currentChar);
        }

        lexed.add(new Token(TokenType.TEXT, stringBuilder.toString()));

        return lexed;
    }

}
