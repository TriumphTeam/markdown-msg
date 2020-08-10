package me.mattstudios.mfmsg.base.internal.token;

public final class TextToken implements Token {

    private final String text;

    public TextToken(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
