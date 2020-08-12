package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.component.StringComponentBuilder;
import me.mattstudios.mfmsg.base.internal.parser.ComponentParser;
import me.mattstudios.mfmsg.base.internal.parser.StringParser;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Set;

public final class Message {

    private final Set<Format> formats;

    private Message(final FormatOptions formatOptions) {
        formats = formatOptions.getFormats();
    }

    public static Message create(final FormatOptions formatOptions) {
        return new Message(formatOptions);
    }

    public static Message create() {
        return new Message(new FormatOptions());
    }

    public String parseToString(final String message) {
        return new StringParser(message, formats).build();
    }

    public BaseComponent[] parseToComponent(final String message) {
        return new ComponentParser(message, formats).build();
    }

}
