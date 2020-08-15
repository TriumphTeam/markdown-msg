package me.mattstudios.mfmsg.base.internal.parser;

import me.mattstudios.mfmsg.base.internal.component.ColorComponent;
import me.mattstudios.mfmsg.base.internal.token.ActionLexer;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.component.BaseComponentAppender;
import me.mattstudios.mfmsg.base.internal.component.Appender;
import me.mattstudios.mfmsg.base.internal.token.ActionToken;
import me.mattstudios.mfmsg.base.internal.token.TextToken;
import me.mattstudios.mfmsg.base.internal.token.Token;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

public final class ComponentParser extends AbstractParser {

    // List of all the generated tokens
    @NotNull
    private final List<Token> tokens;

    private final Set<Format> formats;

    // Main component builder
    @NotNull
    private final ComponentBuilder finalBuilder = new ComponentBuilder();

    private final ColorComponent colorComponent = new ColorComponent();
    private final Appender<BaseComponent[]> appender = new BaseComponentAppender(colorComponent);
    private final MarkdownVisitor visitor;

    public ComponentParser(@NotNull final String message, @NotNull Set<Format> formats) {
        this.formats = formats;

        visitor = new MarkdownVisitor(appender, this.formats);
        tokens = ActionLexer.tokenize(message);
        parseTokens();
    }

    /**
     * Method to parse all the tokens
     */
    private void parseTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            final Token token = tokens.get(i);
            // Checks whether or not the token is an action
            if (token instanceof ActionToken) {
                parseAction((ActionToken) token);
                if (i < tokens.size() - 1) appendSpace();
                continue;
            }

            // Trims and checks if the text is empty
            final String tokenText = ((TextToken) token).getText().trim();
            if (tokenText.isEmpty()) continue;
            // Parses a normal text instead
            visitor.visitComponents(PARSER.parse(tokenText));
            finalBuilder.append(appender.build(), ComponentBuilder.FormatRetention.NONE);
            if (i < tokens.size() - 1) appendSpace();
        }
    }

    /**
     * Appends a space after each token, only required because {@link MarkdownVisitor} will trim the spaces
     */
    private void appendSpace() {
        finalBuilder.append(TextComponent.fromLegacyText(" "), ComponentBuilder.FormatRetention.NONE);
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
                    final Appender<BaseComponent[]> appender = new BaseComponentAppender(new ColorComponent());
                    final MarkdownVisitor visitor = new MarkdownVisitor(appender, formats);
                    visitor.visitComponents(PARSER.parse(actionText));
                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, appender.build());
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
        appender.setClickEvent(clickEvent);
        appender.setHoverEvent(hoverEvent);

        visitor.visitComponents(PARSER.parse(token.getActionText()));
        final BaseComponent[] baseComponent = appender.build();
        if (baseComponent.length == 0) return;
        finalBuilder.append(baseComponent, ComponentBuilder.FormatRetention.NONE);
    }

    public BaseComponent[] build() {
        return finalBuilder.create();
    }

}