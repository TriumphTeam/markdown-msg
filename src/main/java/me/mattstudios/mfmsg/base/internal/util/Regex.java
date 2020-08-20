package me.mattstudios.mfmsg.base.internal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Regex {

    public static final Pattern NEW_LINE = Pattern.compile("\\r?\\\\n");
    public static final Pattern ACTION_PATTERN = Pattern.compile("((?<start>[ ]*)((?<!\\\\)\\[(?<text>.+?)(?<!\\\\)](?<!\\\\)\\((?<actions>.+?)(?<!\\\\)\\))(?<end>[ ]*))");

    private static final Pattern NEW_LINE_ACTION = Pattern.compile("(?<action>\\[[^]]*]\\([^)]*\\))|(?<break>\\r?\\\\n)");

    private Regex() {}

    public static List<String> splitNewLine(final String message) {
        final List<String> lines = new ArrayList<>();
        final Matcher matcher = NEW_LINE_ACTION.matcher(message);

        String rest = message;
        int start = 0;
        while (matcher.find()) {
            if (matcher.group("action") != null) continue;

            final String before = message.substring(start, matcher.start());
            if (!before.isEmpty()) lines.add(before.trim());

            start = matcher.end();
            rest = message.substring(start);
        }

        if (!rest.isEmpty()) lines.add(rest.trim());

        return lines;
    }

}
