package me.mattstudios.msg.bukkit.serializer;

import me.mattstudios.msg.base.internal.nodes.MessageNode;
import me.mattstudios.msg.base.serializer.Appender;
import me.mattstudios.msg.base.serializer.NodeScanner;
import me.mattstudios.msg.bukkit.JsonAppender;
import me.mattstudios.msg.bukkit.StringAppender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class NodeSerializer {

    private NodeSerializer() {}

    public static String toJson(@NotNull final List<MessageNode> nodeList) {
        final Appender<String> jsonAppender = new JsonAppender();
        NodeScanner.scan(nodeList, jsonAppender);
        return jsonAppender.build();
    }

    public static String toString(@NotNull final List<MessageNode> nodeList) {
        final Appender<String> jsonAppender = new StringAppender();
        NodeScanner.scan(nodeList, jsonAppender);
        return jsonAppender.build();
    }

}
