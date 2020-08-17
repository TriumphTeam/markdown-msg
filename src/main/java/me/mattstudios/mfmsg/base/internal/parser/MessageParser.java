package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.component.MessageAppender;
import me.mattstudios.mfmsg.base.internal.color.ColorHandler;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.token.ActionLexer;
import me.mattstudios.mfmsg.base.internal.token.ActionToken;
import me.mattstudios.mfmsg.base.internal.token.TextToken;
import me.mattstudios.mfmsg.base.internal.token.Token;
import net.md_5.bungee.api.chat.*;
import org.commonmark.node.Node;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

public final class MessageParser extends AbstractParser {

    // List of all the generated tokens
    @NotNull
    private final List<Token> tokens;

    private final List<MessagePart> parts = new ArrayList<>();

    private final ColorHandler colorHandler = new ColorHandler();
    private final Appender appender = new MessageAppender(colorHandler);
    private final MarkdownVisitor visitor;

    public MessageParser(@NotNull final String message, @NotNull Set<Format> formats) {
        visitor = new MarkdownVisitor(formats);
        tokens = ActionLexer.tokenize(message);
        parseTokens();
    }

    /**
     * Method to parse all the tokens
     */
    private void parseTokens() {
        for (final Token token : tokens) {
            // Checks whether or not the token is an action
            if (token instanceof ActionToken) {
                parseAction((ActionToken) token);
                continue;
            }

            // Trims and checks if the text is empty
            final String tokenText = ((TextToken) token).getText().trim();
            if (tokenText.isEmpty()) continue;
            // Parses a normal text instead
            visit(PARSER.parse(tokenText));
            parts.addAll(appender.build());
        }
    }

    private void visit(final Node node) {
        visitor.visitComponents(node, appender);
    }

    /**
     * Handles the action parsing
     *
     * @param token The current {@link ActionToken} to parse
     */
    private void parseAction(@NotNull final ActionToken token) {
        HoverEvent hoverEvent = null;
        ClickEvent clickEvent = null;

        // Splits the token message on "|" to separate it's types
        for (final String action : SPLIT_PATTERN.split(token.getActions())) {
            final Matcher matcher = ACTION_PATTERN.matcher(action.trim());

            // If nothing is matched continue
            if (!matcher.find()) continue;

            final String actionText = matcher.group("text").trim();

            // Checks for which action type it is
            switch (matcher.group("type").toLowerCase()) {
                case "hover":
                    // Parses the action text
                    //final Appender appender = new MessageAppender(new ColorHandler());
                    //visitor.visitComponents(PARSER.parse(actionText), appender);
                    //hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, appender.build());
                    break;

                case "command":
                    clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, actionText);
                    break;

                case "suggest":
                    clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, actionText);
                    break;

                case "clipboard":
                    clickEvent = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, actionText);
                    break;

                case "url":
                    clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, actionText);
                    break;
            }
        }

        // Adds the click and hover events
        //appender.setClickEvent(clickEvent);
        //appender.setHoverEvent(hoverEvent);

        visit(PARSER.parse(token.getActionText()));
        final List<MessagePart> parts = appender.build();
        if (parts.isEmpty()) return;
        this.parts.addAll(appender.build());
    }

    public List<MessagePart> build() {
        return parts;
    }

}
