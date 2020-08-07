package me.mattstudios.mfmsg.base;

public enum TokenType {

    ASTERISK,
    ESCAPE,
    L_ARROW,
    HASH_TAG,
    R_ARROW,
    L_BRACKET,
    R_BRACKET,
    L_PAREN,
    R_PAREN,
    DECLARE,
    TEXT,
    PARSABLE;


    public static TokenType[] possibleValues() {
        return new TokenType[] {ASTERISK, ESCAPE, L_ARROW, R_ARROW, L_BRACKET, R_BRACKET, L_PAREN, R_PAREN, DECLARE, TEXT};
    }

}
