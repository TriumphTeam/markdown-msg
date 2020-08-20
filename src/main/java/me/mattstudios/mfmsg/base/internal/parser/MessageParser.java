package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.action.ClickAction;
import me.mattstudios.mfmsg.base.internal.action.HoverAction;
import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.component.MessageAppender;
import me.mattstudios.mfmsg.base.internal.component.MessagePart;
import me.mattstudios.mfmsg.base.internal.extension.ObfuscatedExtension;
import me.mattstudios.mfmsg.base.internal.extension.UnderlineExtension;
import me.mattstudios.mfmsg.base.internal.token.ActionLexer;
import me.mattstudios.mfmsg.base.internal.token.ActionToken;
import me.mattstudios.mfmsg.base.internal.token.SpaceToken;
import me.mattstudios.mfmsg.base.internal.token.TextToken;
import me.mattstudios.mfmsg.base.internal.token.Token;
import me.mattstudios.mfmsg.base.internal.util.RegexUtils;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

public final class MessageParser {

    // The parser that'll be used with the strikethrough, underline, obfuscated extensions
    private static final Parser PARSER = Parser.builder().extensions(Arrays.asList(StrikethroughExtension.create(), UnderlineExtension.create(), ObfuscatedExtension.create())).build();

    // List of all the generated tokens
    @NotNull
    private final List<Token> tokens;

    private final Set<Format> formats;

    private final List<MessagePart> parts = new ArrayList<>();

    private final Appender appender = new MessageAppender();
    private final MarkdownVisitor visitor;

    public MessageParser(@NotNull final String message, @NotNull Set<Format> formats) {
        this.formats = formats;

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

            if (token instanceof SpaceToken) {
                appender.append(((SpaceToken) token).getText(), false, false, false, false, false);
                parts.addAll(appender.build());
                continue;
            }

            // Trims and checks if the text is empty
            final String tokenText = ((TextToken) token).getText();
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
        final List<Action> actions = new ArrayList<>(2);

        // Splits the token message on "|" to separate it's types
        for (final String action : RegexUtils.SPLIT_PATTERN.split(token.getActions())) {
            final Matcher matcher = RegexUtils.ACTION_PATTERN.matcher(action);

            // If nothing is matched continue
            if (!matcher.find()) continue;

            final String actionText = matcher.group("text").trim();

            // Checks for which action type it is
            switch (matcher.group("type").toLowerCase()) {
                case "hover":
                    if (!formats.contains(Format.ACTION_HOVER)) break;
                    
                    final List<List<MessagePart>> parts = new ArrayList<>();
                    for (final String line : RegexUtils.NEW_LINE.split(actionText)) {
                        final Appender appender = new MessageAppender();
                        visitor.visitComponents(PARSER.parse(line), appender);
                        parts.add(appender.build());
                    }
                    actions.add(new HoverAction(parts));
                    break;

                case "command":
                    actions.add(new ClickAction(Format.ACTION_COMMAND, actionText));
                    break;

                case "suggest":
                    actions.add(new ClickAction(Format.ACTION_SUGGEST, actionText));
                    break;

                case "clipboard":
                    actions.add(new ClickAction(Format.ACTION_CLIPBOARD, actionText));
                    break;

                case "url":
                    actions.add(new ClickAction(Format.ACTION_URL, actionText));
                    break;
            }
        }

        // Adds the click and hover events
        appender.addActions(actions);
        visit(PARSER.parse(token.getActionText()));
        final List<MessagePart> parts = appender.build();
        if (parts.isEmpty()) return;
        this.parts.addAll(parts);
    }

    public List<MessagePart> parse() {
        return parts;
    }

}
