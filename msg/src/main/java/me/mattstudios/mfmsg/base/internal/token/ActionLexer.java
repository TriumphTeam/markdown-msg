package me.mattstudios.mfmsg.base.internal.token;

import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Will lex and separate the message into actions and not actions
 */
public final class ActionLexer {

    private ActionLexer() {}

    /**
     * Tokenizes the message into the correct tokens
     *
     * @param message The message to parse
     * @return The list with the generated tokens
     */
    @NotNull
    public static List<Token> tokenize(@NotNull final String message) {
        final List<Token> tokens = new LinkedList<>();
        final Matcher matcher = RegexUtils.ACTION_PATTERN_SPACES.matcher(message);

        String rest = message;
        int start = 0;
        while (matcher.find()) {

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) tokens.add(new TextToken(before));

            final String startSpaces = matcher.group("start");
            if (startSpaces != null && !startSpaces.isEmpty()) {
                tokens.add(new SpaceToken(startSpaces));
            }
            
            final String actionText = matcher.group("text");
            final String actions = matcher.group("actions");

            tokens.add(new ActionToken(actionText, actions));

            final String endSpaces = matcher.group("end");
            if (endSpaces != null && !endSpaces.isEmpty()) {
                tokens.add(new SpaceToken(endSpaces));
            }

            start = matcher.end();
            rest = message.substring(start);
        }

        if (!rest.isEmpty()) tokens.add(new TextToken(rest));

        return tokens;
    }

}
