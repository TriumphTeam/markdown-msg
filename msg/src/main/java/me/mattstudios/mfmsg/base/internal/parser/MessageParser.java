package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.action.Action;
import me.mattstudios.mfmsg.base.internal.action.ClickAction;
import me.mattstudios.mfmsg.base.internal.action.HoverAction;
import me.mattstudios.mfmsg.base.internal.color.MessageColor;
import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.component.MessageAppender;
import me.mattstudios.mfmsg.base.internal.component.MessageLine;
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
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Main parser used
 */
public final class MessageParser {

    // The parser that'll be used with the strikethrough, underline, obfuscated extensions
    private static final Parser PARSER = Parser.builder().extensions(Arrays.asList(StrikethroughExtension.create(), UnderlineExtension.create(), ObfuscatedExtension.create())).build();

    // List of all the generated tokens
    @NotNull
    private final List<Token> tokens;

    @NotNull
    private final Set<Format> formats;
    @NotNull
    private final MessageColor defaultColor;

    @NotNull
    private final List<MessagePart> parts = new ArrayList<>();

    @NotNull
    private final Appender appender;
    @NotNull
    private final MarkdownVisitor visitor;

    /**
     * Main constructor that takes message formats and default color
     *
     * @param message      The message to parse
     * @param formats      The formats to check for
     * @param defaultColor The default color
     */
    public MessageParser(@NotNull final String message, @NotNull Set<Format> formats, @NotNull final MessageColor defaultColor) {
        this.formats = formats;
        this.defaultColor = defaultColor;

        appender = new MessageAppender(formats, defaultColor);
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

            // Simply appends a space because of the actions and commonmark
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

    /**
     * Visits the given node
     *
     * @param node The node to visit
     */
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

                    final List<MessageLine> lines = new ArrayList<>();
                    for (final String line : RegexUtils.NEW_LINE.split(actionText)) {
                        final Appender appender = new MessageAppender(formats, defaultColor);
                        visitor.visitComponents(PARSER.parse(line), appender);
                        lines.add(new MessageLine(appender.build()));
                    }
                    actions.add(new HoverAction(lines));
                    break;

                case "command":
                    if (!formats.contains(Format.ACTION_COMMAND)) break;
                    actions.add(new ClickAction(Format.ACTION_COMMAND, actionText));
                    break;

                case "suggest":
                    if (!formats.contains(Format.ACTION_SUGGEST)) break;
                    actions.add(new ClickAction(Format.ACTION_SUGGEST, actionText));
                    break;

                case "clipboard":
                    if (!formats.contains(Format.ACTION_CLIPBOARD)) break;
                    actions.add(new ClickAction(Format.ACTION_CLIPBOARD, actionText));
                    break;

                case "url":
                    if (!formats.contains(Format.ACTION_URL)) break;
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

    /**
     * Builds the parsed information into the list of parts
     *
     * @return The list of all the parsed parts
     */
    public List<MessagePart> build() {
        return parts;
    }

}
