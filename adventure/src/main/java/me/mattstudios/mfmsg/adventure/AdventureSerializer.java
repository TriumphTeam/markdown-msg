package me.mattstudios.mfmsg.adventure;

import me.mattstudios.mfmsg.adventure.appender.AdventureAppender;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.Appender;
import me.mattstudios.mfmsg.base.serializer.scanner.ScanUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class AdventureSerializer {

    /**
     * Serializes the nodes into components
     *
     * @param nodeList A list of {@link MessageNode}
     * @return A Kyori {@link Component}
     */
    static Component toComponent(@NotNull final List<MessageNode> nodeList) {
        final Appender<Component> adventureAppender = new AdventureAppender();
        ScanUtils.scan(nodeList, adventureAppender);
        return adventureAppender.build();
    }

}
