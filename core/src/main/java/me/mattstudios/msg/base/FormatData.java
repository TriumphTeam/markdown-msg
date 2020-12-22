package me.mattstudios.msg.base;

import me.mattstudios.msg.base.internal.color.FlatColor;
import me.mattstudios.msg.base.internal.color.MessageColor;
import org.jetbrains.annotations.NotNull;

public final class FormatData {

    @NotNull
    private MessageColor color = new FlatColor("white");

    private boolean bold = false;
    private boolean italic = false;
    private boolean strike = false;
    private boolean underlined = false;
    private boolean obfuscated = false;

    @NotNull
    public MessageColor getColor() {
        return color;
    }

    public void setColor(@NotNull final MessageColor color) {
        this.color = color;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(final boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(final boolean italic) {
        this.italic = italic;
    }

    public boolean isStrike() {
        return strike;
    }

    public void setStrike(final boolean strike) {
        this.strike = strike;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public void setUnderlined(final boolean underlined) {
        this.underlined = underlined;
    }

    public boolean isObfuscated() {
        return obfuscated;
    }

    public void setObfuscated(final boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

}
