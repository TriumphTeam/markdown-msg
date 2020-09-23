package me.mattstudios.mfmsg.bukkit.serializer;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.Appender;
import me.mattstudios.mfmsg.bukkit.appender.JsonAppender;
import me.mattstudios.mfmsg.bukkit.appender.StringAppender;
import me.mattstudios.mfmsg.base.serializer.scanner.ScanUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class NodeSerializer {

    private NodeSerializer() {}

    public static String toJson(@NotNull final List<MessageNode> nodeList) {
        final Appender jsonAppender = new JsonAppender();
        ScanUtils.scan(nodeList, jsonAppender);
        return jsonAppender.build();
    }

    public static String toString(@NotNull final List<MessageNode> nodeList) {
        final Appender jsonAppender = new StringAppender();
        ScanUtils.scan(nodeList, jsonAppender);
        return jsonAppender.build();
    }

}
