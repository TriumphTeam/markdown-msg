package me.mattstudios.mfmsg.adventure;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;
import me.mattstudios.mfmsg.base.internal.util.Version;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public final class AdventureMessage {

    @NotNull
    private final MessageOptions messageOptions;


    private AdventureMessage(@NotNull final MessageOptions messageOptions) {
        this.messageOptions = messageOptions;
    }

    @NotNull
    public static AdventureMessage create(@NotNull final MessageOptions messageOptions) {
        return new AdventureMessage(messageOptions);
    }

    @NotNull
    public static AdventureMessage create() {
        return new AdventureMessage(new MessageOptions.Builder(EnumSet.allOf(Format.class)).build());
    }

    public Component parse(@NotNull final String message) {
        // Passing in Latest version, as it's not needed to check before since adventure will already convert it
        final MessageParser parser = new MessageParser(messageOptions, Version.V1_16_R2);
        parser.parse(message);
        return AdventureSerializer.toComponent(parser.build());
    }

}
