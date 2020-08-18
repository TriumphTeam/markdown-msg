package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MessageComponent;
import me.mattstudios.mfmsg.base.internal.parser.MessageParser;

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

    public MessageComponent parse(final String message) {
        return new MessageParser(message, formats).parse();
    }

}
