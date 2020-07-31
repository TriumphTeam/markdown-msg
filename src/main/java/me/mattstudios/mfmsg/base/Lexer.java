package me.mattstudios.mfmsg.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Lexer {

    private static final Pattern stylePattern = Pattern.compile("(?<HEX><#.+?>)|(?<ACTION>(?<!\\\\)\\[.+?(?<!\\\\)](?<!\\\\)\\(.+?(?<!\\\\)\\))|(?<BI>(?<!\\\\)\\*+.+?(?<!\\\\)\\*+)|(?<STRIKE>(?<!\\\\)~+.+?~+)|(?<ESCAPED>\\\\[*~\\[\\]()])");
    private static final List<TokenType> tokenTypes = Arrays.asList(TokenType.values()).parallelStream().filter(tokenType -> tokenType != TokenType.TEXT).collect(Collectors.toList());

    public List<Token> lex(final String input) {
        final List<Token> lexed = new ArrayList<>();

        final Matcher matcher = stylePattern.matcher(input);

        int lastStart = 0;
        while (matcher.find()) {

            lexed.add(new Token(TokenType.TEXT, input.substring(lastStart, matcher.start())));
            lastStart = matcher.end();

            for (final TokenType tokenType : tokenTypes) {
                final String group = matcher.group(tokenType.name());
                if (group == null) continue;

                lexed.add(new Token(tokenType, group));
            }

        }

        lexed.add(new Token(TokenType.TEXT, input.substring(lastStart)));

        return lexed;
    }

}
