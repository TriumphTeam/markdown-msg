package me.mattstudios.mfmsg.adventure;

import me.mattstudios.mfmsg.adventure.appender.AdventureAppender;
import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.serializer.scanner.ScanUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class AdventureSerializer {

    static Component toComponent(@NotNull final List<MessageNode> nodeList) {
        final AdventureAppender adventureAppender = new AdventureAppender();
        ScanUtils.scan(nodeList, adventureAppender);
        return adventureAppender.buildComponent();
    }

}
