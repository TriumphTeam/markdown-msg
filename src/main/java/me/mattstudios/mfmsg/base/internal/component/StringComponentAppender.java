package me.mattstudios.mfmsg.base.internal.component;

import org.jetbrains.annotations.NotNull;

public final class StringComponentAppender implements Appender<String> {

    private StringBuilder builder = new StringBuilder();

    @Override
    public void append(final @NotNull String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated) {

        if (bold) builder.append("§l");
        if (italic) builder.append("§o");
        if (strike) builder.append("§m");
        if (underline) builder.append("§n");
        if (obfuscated) builder.append("§k");

        appendString(message);
    }

    @Override
    public String build() {
        final String buildString = builder.toString();
        builder = new StringBuilder();
        return buildString;
    }

    private void appendString(final String message) {
        builder.append(message).append("§r");
    }

}
