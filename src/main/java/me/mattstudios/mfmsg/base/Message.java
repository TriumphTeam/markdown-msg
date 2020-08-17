package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;

import java.util.List;
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

    public List<MessagePart> parse(final String message) {
        return new MessageParser(message, formats).build();
    }

}
