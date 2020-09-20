package me.mattstudios.mfmsg.commonmark.internal.inline.mf;

import me.mattstudios.mfmsg.commonmark.internal.inline.Scanner;
import me.mattstudios.mfmsg.commonmark.internal.util.Parsing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ActionScanner {

    @NotNull
    private static final List<String> ACTIONS = Arrays.asList("COMMAND", "HOVER", "URL", "SUGGEST", "CLIPBOARD");

    public static Map<String, String> scanAction(@NotNull final Scanner scanner) {
        // Counts parenthesis
        int parens = 0;

        // Holds the actions found
        final Map<String, String> actions = new LinkedHashMap<>();
        final StringBuilder builder = new StringBuilder();

        String type = null;
        // Loops through the next characters
        while (scanner.hasNext()) {
            boolean foundEscape = false;
            final char c = scanner.peek();

            // Handles
            if (c == '\\') {
                scanner.next();
                if (Parsing.isEscapable(scanner.peek())) {
                    builder.append(scanner.peek());
                    scanner.next();
                    continue;
                }

                foundEscape = true;
            }

            if (c == ':') {
                type = builder.toString();

                if (!ACTIONS.contains(type.toUpperCase())) {
                    return actions;
                }

                builder.setLength(0);
                scanner.next();
                continue;
            }

            if (c == '|') {
                add(type, builder.toString().trim(), actions);
                type = null;

                builder.setLength(0);
                scanner.next();
                continue;
            }

            if (c == '(') {
                parens++;
                // Limit to 32 nested parens for pathological cases
                if (parens > 32) {
                    add(type, builder.toString().trim(), actions);
                    return actions;
                }
            }

            if (c == ')') {
                if (parens == 0) {
                    add(type, builder.toString().trim(), actions);
                    return actions;
                }

                parens--;

                builder.append(c);
                scanner.next();
                continue;
            }

            builder.append(c);
            if (!foundEscape) scanner.next();
        }

        return actions;
    }

    private static void add(@Nullable final String type, @NotNull final String value, @NotNull final Map<String, String> actions) {
        if (type != null && value.isEmpty()) {
            actions.put(type, value);
        }
    }

}