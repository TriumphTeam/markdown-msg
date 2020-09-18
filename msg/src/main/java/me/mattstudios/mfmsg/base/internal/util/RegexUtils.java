package me.mattstudios.mfmsg.base.internal.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {

    // Pattern for acceptable colors
    public static final Pattern COLOR_PATTERN = Pattern.compile("(?<!\\\\)[<&](?<hex>#[A-Fa-f0-9]{3,6})[>]?|(?<!\\\\)&(?<char>[a-fA-F0-9r])|<(?:g|gradient):(?<gradient>.+?)>|<(?<r>r|rainbow)(?<sat>:\\d*\\.\\d*)?(?<lig>:\\d*\\.\\d*)?>");
    // Pattern for turning #000 into #000000
    public static final Pattern THREE_HEX = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");

    // Pattern for action splitting
    public static final Pattern SPLIT_PATTERN = Pattern.compile("(?<!\\\\)\\|");
    // Pattern for the action type and text
    public static final Pattern ACTION_PATTERN = Pattern.compile("^(?<type>\\w+):(?<text>.*)");
    // Pattern for new line splitting
    public static final Pattern NEW_LINE = Pattern.compile("\\r?\\\\n");
    // Pattern for action with spaces for tokens
    public static final Pattern ACTION_PATTERN_SPACES = Pattern.compile("((?<start>[ ]*)((?<!\\\\)\\[(?<text>.+?)(?<!\\\\)](?<!\\\\)\\((?<actions>.+?)(?<!\\\\)\\))(?<end>[ ]*))");
    // Pattern for all characters
    public static final Pattern CHARACTER = Pattern.compile(".");

    // Pattern for splitting with \n but ignoring action's new lines
    private static final Pattern NEW_LINE_ACTION = Pattern.compile("(?<action>\\[[^]]*]\\([^)]*\\))|(?<break>\\r?\\\\n)");

    private RegexUtils() {}

    /**
     * Splits the message and ignores \n that are in the action
     *
     * @param message The message to split
     * @return The list with the new lines
     */
    @NotNull
    public static List<String> splitNewLine(@NotNull final String message) {
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
