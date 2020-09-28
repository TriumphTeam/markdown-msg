package me.mattstudios.mfmsg.adventure;

import me.mattstudios.mfmsg.adventure.appender.AdventureAppender;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.Appender;
import me.mattstudios.mfmsg.base.serializer.scanner.NodeScanner;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class AdventureSerializer {

    /**
     * Serializes the nodes into components
     *
     * @param nodeList A list of {@link MessageNode}
     * @return A Kyori {@link Component}
     */
    public static Component toComponent(@NotNull final List<MessageNode> nodeList) {
        final Appender<Component> adventureAppender = new AdventureAppender();
        NodeScanner.scan(nodeList, adventureAppender);
        return adventureAppender.build();
    }

}
