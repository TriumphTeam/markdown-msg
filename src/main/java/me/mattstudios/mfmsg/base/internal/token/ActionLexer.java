package me.mattstudios.mfmsg.base.internal.token;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ActionLexer {

    private ActionLexer() {}

    private static final Pattern ACTION_PATTERN = Pattern.compile("((?<start>[ ]*)((?<!\\\\)\\[(?<text>.+?)(?<!\\\\)](?<!\\\\)\\((?<actions>.+?)(?<!\\\\)\\))(?<end>[ ]*))");

    public static List<Token> tokenize(@NotNull final String text) {
        final List<Token> tokens = new LinkedList<>();
        final Matcher matcher = ACTION_PATTERN.matcher(text);

        String rest = text;
        int start = 0;
        while (matcher.find()) {

            final String before = text.substring(start, matcher.start());
            if (!before.isEmpty()) tokens.add(new TextToken(before));

            final String startSpaces = matcher.group("start");
            if (startSpaces != null && !startSpaces.isEmpty()) {
                tokens.add(new SpaceToken(startSpaces));
            }

            // TODO - Handle nulls later
            final String actionText = matcher.group("text");
            final String actions = matcher.group("actions");

            tokens.add(new ActionToken(actionText, actions));

            final String endSpaces = matcher.group("end");
            if (endSpaces != null && !endSpaces.isEmpty()) {
                tokens.add(new SpaceToken(endSpaces));
            }

            start = matcher.end();
            rest = text.substring(start);
        }

        if (!rest.isEmpty()) tokens.add(new TextToken(rest));

        return tokens;
    }

}
