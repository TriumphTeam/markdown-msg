package me.mattstudios.mfmsg.base;

public enum TokenType {

    ASTERISK('*'),
    ESCAPE('\\'),
    L_ARROW('<'),
    R_ARROW('>'),
    L_BRACKET('['),
    R_BRACKET(']'),
    L_PAREN('('),
    R_PAREN(')'),
    DECLARE(':'),
    TEXT(null),
    PARSABLE(null);


    public static TokenType[] possibleValues() {
        return new TokenType[] {ASTERISK, ESCAPE, L_ARROW, R_ARROW, L_BRACKET, R_BRACKET, L_PAREN, R_PAREN, DECLARE, TEXT};
    }

    private final Character character;

    TokenType(final Character character) {
        this.character = character;
    }

    public Character getChar() {
        return character;
    }

}
