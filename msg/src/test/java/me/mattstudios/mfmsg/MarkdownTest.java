package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import me.mattstudios.mfmsg.base.internal.util.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public final class MarkdownTest {

    @Test
    public void test_bold() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("**Hello**");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining(", "));

        Assertions.assertEquals(nodeString, "Hello - BOLD");
    }

    @Test
    public void test_italic() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("*Hello*");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining(", "));

        Assertions.assertEquals(nodeString, "Hello - ITALIC");
    }

    @Test
    public void test_strikethrough() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("~~Hello~~");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining(", "));

        Assertions.assertEquals(nodeString, "Hello - STRIKETHROUGH");
    }

    @Test
    public void test_underlined() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("__Hello__");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining(", "));

        Assertions.assertEquals(nodeString, "Hello - UNDERLINED");
    }

    @Test
    public void test_obfuscated() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("||Hello||");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining(", "));

        Assertions.assertEquals(nodeString, "Hello - OBFUSCATED");
    }

    @Test
    public void test_action() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("[Hello](hover: Hello!)");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining(", "));

        Assertions.assertEquals(nodeString, "Hello - ACTIONS");
    }

    @Test
    public void test_complex() {
        final MessageOptions options = new MessageOptions.Builder(EnumSet.allOf(Format.class)).build();
        final MessageParser messageParser = new MessageParser(options, Version.V1_16_R2);

        messageParser.parse("**Bold** *italic* ~~strike~~ __underline__ ||obfuscated|| [action](hover: Hello!)");

        final List<MessageNode> nodes = messageParser.build();
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining());

        Assertions.assertEquals(nodeString, "Bold - BOLD italic - ITALIC strike - STRIKETHROUGH underline - UNDERLINED obfuscated - OBFUSCATED action - ACTIONS");
    }

}
