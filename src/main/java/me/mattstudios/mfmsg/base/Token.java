package me.mattstudios.mfmsg.base;

public final class Token {

    private final TokenType tokenType;
    private final String value;

    public Token(final TokenType tokenType, final String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    /**
     * Testing only
     */
    public String get() {
        return "[" + tokenType.name() + " = " + value + "]";
    }

    public TokenType getType() {
        return tokenType;
    }

}
