package me.mattstudios.mfmsg.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Lexer {

    private Lexer() {}

    private static final Map<Character, TokenType> TOKENS = new LinkedHashMap<>();

    static {
        TOKENS.put('*', TokenType.ASTERISK);
        TOKENS.put('\\', TokenType.ESCAPE);
        TOKENS.put('<', TokenType.L_ARROW);
        TOKENS.put('#', TokenType.HASH_TAG);
        TOKENS.put('>', TokenType.R_ARROW);
        TOKENS.put('[', TokenType.L_BRACKET);
        TOKENS.put(']', TokenType.R_BRACKET);
        TOKENS.put('(', TokenType.L_PAREN);
        TOKENS.put(')', TokenType.R_PAREN);
    }

    public static List<Token> lex(final String input) {
        final List<Token> lexed = new ArrayList<>();

        final char[] chars = input.toCharArray();

        // Builder to capture the TEXT token
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            final char currentChar = chars[i];

            // If the current token type is null it's a TEXT token
            final TokenType tokenType = TOKENS.get(currentChar);
            if (tokenType == null || (i + 1 >= chars.length)) {
                textBuilder.append(currentChar);
                continue;
            }

            // Checks for possible escaping
            if (tokenType == TokenType.ESCAPE) {
                final char nextChar = chars[i + 1];
                final TokenType nextToken = TOKENS.get(nextChar);

                // Appends "\", as it's not followed by another token
                if (nextToken == null) {
                    textBuilder.append(currentChar);
                    continue;
                }

                // Appends only the next char as it's been escaped
                textBuilder.append(nextChar);
                i = i + 1;
                continue;
            }

            // Appends the text and resets the StringBuilder
            if (textBuilder.length() != 0) {
                lexed.add(new Token(TokenType.TEXT, textBuilder.toString()));
                textBuilder = new StringBuilder();
            }

            lexed.add(new Token(tokenType, String.valueOf(currentChar)));
        }

        if (textBuilder.length() != 0) {
            lexed.add(new Token(TokenType.TEXT, textBuilder.toString()));
        }

        return lexed;
    }

}