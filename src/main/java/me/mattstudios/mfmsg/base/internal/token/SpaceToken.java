package me.mattstudios.mfmsg.base.internal.token;

public final class SpaceToken implements Token {

    private final String text;

    public SpaceToken(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
