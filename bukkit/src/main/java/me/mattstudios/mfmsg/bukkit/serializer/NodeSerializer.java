package me.mattstudios.mfmsg.bukkit.serializer;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.Appender;
import me.mattstudios.mfmsg.base.serializer.NodeScanner;
import me.mattstudios.mfmsg.bukkit.JsonAppender;
import me.mattstudios.mfmsg.bukkit.StringAppender;
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
