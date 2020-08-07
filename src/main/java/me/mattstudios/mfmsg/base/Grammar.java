package me.mattstudios.mfmsg.base;

import java.util.Stack;

public enum Grammar {
    COLOR(
            //       <             #123456             >
            TokenType.L_ARROW, TokenType.TEXT, TokenType.R_ARROW
    ),
    BOXED_TEXT(
            //       [                 ...               ]
            TokenType.L_BRACKET, TokenType.PARSABLE, TokenType.R_BRACKET
    ),
    ACTION(
            //      [                 ...               ]                       (                 action              :                ...                 )
            TokenType.L_BRACKET, TokenType.PARSABLE, TokenType.R_BRACKET, TokenType.L_PAREN, TokenType.TEXT, TokenType.DECLARE, TokenType.PARSABLE, TokenType.R_PAREN
    ),
    BOLD(
            //       *                ...                   *
            TokenType.ASTERISK, TokenType.PARSABLE, TokenType.ASTERISK
    );

    private final TokenType[] tokenGrammar;
    private boolean hasVarArg;


    /**
     * Define grammar rules for specific grammar.
     * A parsable token must have a prefix and suffix
     */
    Grammar(TokenType... tokenGrammar) {
        this.tokenGrammar = tokenGrammar;
        // Check for presence of vararg tokens.
        for (TokenType type: tokenGrammar) {
            if (type == TokenType.PARSABLE) {
                hasVarArg = true;
                return;
            }
        }
        hasVarArg = false;
    }

    /**
     * Check if it presented stack at least matches an initial portion of a grammar
     * @param tokenStack currently traversed tokens
     * @return true, if input follows this grammar
     */
    public boolean partialMatches(Stack<Token> tokenStack) {
        // Cant predict size if any variable argument sized grammar is used
        if (!hasVarArg) {
            // There are more tokens than grammar length, so cannot be following this grammar
            if (tokenStack.size() > tokenGrammar.length) return false;
        }
        int index = 0;
        int depth = 0; // Used when a vararg prefix/suffix might be repeated inside to find correct closure to close

        for (Token token: tokenStack) {
            if (index > tokenGrammar.length) {
                // Partial match successful, Did not fail till end of token stack so it still passes
                return true;
            }
            // Parsable tokens being varargs, continue at this grammar token till suffix is found
            if (tokenGrammar[index] == TokenType.PARSABLE) {
                if (tokenGrammar[index - 1] == tokenGrammar[index + 1] && token.getType() == tokenGrammar[index - 1]) {
                    // Prefix and suffix are same, consider next token of that type to close
                    index++;
                } else {
                    // Prefix and suffix are different, use depth to find proper closing token
                    if (token.getType() == tokenGrammar[index - 1]) {
                        depth++;
                    } else if (token.getType() == tokenGrammar[index + 1] && --depth < 0) {
                        index++;
                        depth = 0;
                    }
                }
                continue;
            }

            if (token.getType() != tokenGrammar[index++]) {
                // Grammar token rule doesnt match with token in stack, therefore grammar does not match
                return false;
            }
        }
        // Check if we ran out of tokens while going though vararg depths
        return depth == 0;
    }

}
