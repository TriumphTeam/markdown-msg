package me.mattstudios.mfmsg.base;

public enum TokenType {

    ASTERISK('*'),
    ESCAPE('\\'),
    TEXT(null);

    private final Character character;

    TokenType(final Character character) {
        this.character = character;
    }

    public Character getChar() {
        return character;
    }

}
