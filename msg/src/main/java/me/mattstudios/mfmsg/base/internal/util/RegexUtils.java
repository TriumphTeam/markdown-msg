package me.mattstudios.mfmsg.base.internal.util;

import java.util.regex.Pattern;

public final class RegexUtils {

    // Pattern for turning #000 into #000000
    public static final Pattern THREE_HEX = Pattern.compile("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");

    private RegexUtils() {}

}
