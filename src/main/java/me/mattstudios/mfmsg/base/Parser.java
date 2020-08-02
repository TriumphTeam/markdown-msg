package me.mattstudios.mfmsg.base;



import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class Parser {
    private final Stack<Token> tokenStack;
    private final StringBuilder resultBuilder = new StringBuilder();
    private final Stack<Token> parseStack = new Stack<>();
    protected Parser(List<Token> tokens) {
        this.tokenStack = new Stack<>();
        tokenStack.addAll(tokens);
    }

    public String parseText() {
        List<Grammar> grammarMatch = new ArrayList<>();
        Token currentToken;
        while ((currentToken = tokenStack.pop()) != null) {
            parseStack.push(currentToken);
            findCurrentGrammarMatches(grammarMatch);
            if (grammarMatch.size() == 1) {

            } else if (grammarMatch.size() == 0) {

            }
        }
        //System.out.println(parseStack.toString());
        return "";
    }

    //TESTING
    public Stack<Token> getStack() {
        return parseStack;
    }

    /**
     * Find all current possibilities for grammar matches
     * @param toPopulate reused list since this is likely called frequently
     */
    private void findCurrentGrammarMatches(List<Grammar> toPopulate) {
        toPopulate.clear();
        for (Grammar grammar: Grammar.values()) {
            if (grammar.partialMatches(parseStack)) {
                toPopulate.add(grammar);
            }
        }
    }
}
