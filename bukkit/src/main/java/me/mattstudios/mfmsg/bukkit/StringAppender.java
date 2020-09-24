package me.mattstudios.mfmsg.bukkit;

import me.mattstudios.mfmsg.base.internal.action.MessageAction;
import me.mattstudios.mfmsg.base.internal.color.handlers.ColorMapping;
import me.mattstudios.mfmsg.base.serializer.Appender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.List;
import java.util.regex.Pattern;

public final class StringAppender implements Appender<String> {

    private static final Pattern CHARACTER = Pattern.compile(".");
    private final StringBuilder builder = new StringBuilder();

    @Override
    public void append(@NotNull final String value) {
        if (value.isEmpty()) return;
        builder.append(value);
    }

    @Override
    public void appendNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (color != null) {
            if (color.startsWith("#")) {
                if (Version.CURRENT_VERSION.isColorLegacy()) {
                    stringBuilder.append("§").append(ColorMapping.fromName(ColorMapping.toLegacy(Color.decode(color))));
                } else {
                    stringBuilder.append("§x").append(CHARACTER.matcher(color.substring(1)).replaceAll("§$0"));
                }
            } else {
                stringBuilder.append("§").append(ColorMapping.fromName(color));
            }
        }

        if (bold) stringBuilder.append("§l");
        if (italic) stringBuilder.append("§o");
        if (strike) stringBuilder.append("§m");
        if (underline) stringBuilder.append("§n");
        if (obfuscated) stringBuilder.append("§k");

        stringBuilder.append(text);
        stringBuilder.append("§r");

        builder.append(stringBuilder.toString());
    }

    @NotNull
    @Override
    public String build() {
        return builder.toString();
    }

}
