package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.extension.ObfuscatedExtension;
import me.mattstudios.mfmsg.base.internal.extension.UnderlineExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.parser.Parser;

import java.util.Arrays;
import java.util.regex.Pattern;

public abstract class AbstractParser {

    // Pattern for splitting actions with "|"
    static final Pattern SPLIT_PATTERN = Pattern.compile("(?<!\\\\)\\|");
    // Pattern for the action type and text
    static final Pattern ACTION_PATTERN = Pattern.compile("^(?<type>\\w+):(?<text>.*)");
    // Commonmark's parser with Strike and Underline extensions
    static final Parser PARSER = Parser.builder().extensions(Arrays.asList(StrikethroughExtension.create(), UnderlineExtension.create(), ObfuscatedExtension.create())).build();

}
