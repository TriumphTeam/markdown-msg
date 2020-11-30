package me.mattstudios.msg;

import me.mattstudios.msg.base.MessageOptions;
import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.internal.parser.MarkdownParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class ColorTest {

    private final MarkdownParser markdownParser = new MarkdownParser(MessageOptions.builder().build());

    @Test
    public void test_red() {
        final List<MessageNode> nodes = markdownParser.parse("&cHello");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "Hello - red");
    }

    @Test
    public void test_red_escaped() {
        final List<MessageNode> nodes = markdownParser.parse("\\&cHello");
        final String nodeString = nodes.stream().map(Object::toString).collect(Collectors.joining()).trim();

        assertNotNull(nodeString);
        assertEquals(nodeString, "&cHello - white");
    }

}
