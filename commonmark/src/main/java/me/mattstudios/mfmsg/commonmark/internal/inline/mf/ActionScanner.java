package me.mattstudios.mfmsg.commonmark.internal.inline.mf;

import me.mattstudios.mfmsg.commonmark.internal.inline.Scanner;
import me.mattstudios.mfmsg.commonmark.internal.util.Parsing;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ActionScanner {

    private static final List<String> ACTIONS = Arrays.asList("COMMAND", "HOVER", "URL", "SUGGEST", "CLIPBOARD");

    public static Map<String, String> scanAction(Scanner scanner) {
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

                if (!ACTIONS.contains(type)) {
                    return actions;
                    // TODO THIS TOMORROW
                }

                builder.setLength(0);
                scanner.next();
                continue;
            }

            if (c == '|') {
                if (type != null && builder.length() != 0) {
                    actions.put(type, builder.toString());
                    type = null;
                }

                builder.setLength(0);
                scanner.next();
                continue;
            }

            if (c == '(') {
                parens++;
                // Limit to 32 nested parens for pathological cases
                if (parens > 32) {
                    //actions.add(builder.toString());
                    break;
                }
            }

            if (c == ')') {
                if (parens == 0) {
                    if (type != null && builder.length() != 0) {
                        actions.put(type, builder.toString());
                        type = null;
                    }
                    //actions.add(builder.toString());
                    break;
                }

                parens--;

                builder.append(c);
                scanner.next();
                continue;
            }

            builder.append(c);
            if (!foundEscape) scanner.next();
        }

        System.out.println(actions);

        return "";
    }

}