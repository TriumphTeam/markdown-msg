package me.mattstudios.msg;

import me.mattstudios.msg.base.MessageOptions;
import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.internal.parser.MarkdownParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class MarkdownTest {

    private final MarkdownParser markdownParser = new MarkdownParser(MessageOptions.builder().build());

    @Test
    public void test_bold() {
        final List<MessageNode> nodes = markdownParser.parse("**Hello**");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - BOLD - white");
    }

    @Test
    public void test_italic() {
        final List<MessageNode> nodes = markdownParser.parse("*Hello*");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - ITALIC - white");
    }

    @Test
    public void test_strikethrough() {
        final List<MessageNode> nodes = markdownParser.parse("~~Hello~~");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - STRIKETHROUGH - white");
    }

    @Test
    public void test_underlined() {
        final List<MessageNode> nodes = markdownParser.parse("__Hello__");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - UNDERLINED - white");
    }

    @Test
    public void test_obfuscated() {
        final List<MessageNode> nodes = markdownParser.parse("||Hello||");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - OBFUSCATED - white");
    }

    @Test
    public void test_action() {
        final List<MessageNode> nodes = markdownParser.parse("[Hello](hover: Hello!)");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - ACTIONS - white");
    }

    @Test
    public void test_complex() {
        final List<MessageNode> nodes = markdownParser.parse("**Bold** *italic* ~~strike~~ __underline__ ||obfuscated|| [action](hover: Hello!)");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Bold - BOLD - white   - white italic - ITALIC - white   - white strike - STRIKETHROUGH - white   - white underline - UNDERLINED - white   - white obfuscated - OBFUSCATED - white   - white action - ACTIONS - white");
    }

}
