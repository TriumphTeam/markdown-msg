package me.mattstudios.mfmsg.base;

import me.mattstudios.mfmsg.base.internal.ActionLexer;
import me.mattstudios.mfmsg.base.internal.MarkdownVisitor;
import me.mattstudios.mfmsg.base.internal.token.ActionToken;
import me.mattstudios.mfmsg.base.internal.token.TextToken;
import me.mattstudios.mfmsg.base.internal.token.Token;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Message {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("(?<!\\\\)\\|");
    private static final Pattern ACTION_PATTERN = Pattern.compile("^(?<type>\\w+):(?<text>.*)");
    private static final Parser PARSER = Parser.builder().extensions(Collections.singletonList(StrikethroughExtension.create())).build();

    private final List<Token> tokens;
    private final ComponentBuilder finalBuilder = new ComponentBuilder();
    private final MarkdownVisitor markdownVisitor = new MarkdownVisitor();

    private Message(final List<Token> tokens) {
        this.tokens = tokens;
        parseAll();
    }

    public static Message parse(@NotNull final String message) {
        return new Message(ActionLexer.tokenize(message));
    }

    private void parseAll() {
        for (int i = 0; i < tokens.size(); i++) {
            final Token token = tokens.get(i);
            if (token instanceof ActionToken) {
                parseAction((ActionToken) token);
                if (i < tokens.size() - 1) appendSpace();
                continue;
            }

            markdownVisitor.render(PARSER.parse(((TextToken) token).getText()));
            finalBuilder.append(markdownVisitor.build(), ComponentBuilder.FormatRetention.NONE);

            if (i < tokens.size() - 1) appendSpace();
        }
    }

    private void appendSpace() {
        finalBuilder.append(TextComponent.fromLegacyText(" "), ComponentBuilder.FormatRetention.NONE);
    }

    private void parseAction(final ActionToken token) {
        final List<HoverEvent> hoverEvents = new ArrayList<>();
        final List<ClickEvent> clickEvents = new ArrayList<>();

        for (final String action : SPLIT_PATTERN.split(token.getActions())) {
            final Matcher matcher = ACTION_PATTERN.matcher(action.trim());

            if (!matcher.find()) continue;

            markdownVisitor.render(PARSER.parse(matcher.group("text")));

            switch (matcher.group("type").toLowerCase()) {
                case "hover":
                    BaseComponent[] components = markdownVisitor.build();
                    for (final BaseComponent component : components) {
                        System.out.println(component);
                    }
                    hoverEvents.add(new HoverEvent(HoverEvent.Action.SHOW_TEXT, components));
                    break;
            }
        }

        markdownVisitor.addClickEvents(clickEvents);
        markdownVisitor.addHoverEvents(hoverEvents);
        markdownVisitor.render(PARSER.parse(token.getActionText()));
        finalBuilder.append(markdownVisitor.build(), ComponentBuilder.FormatRetention.NONE);
    }

    public BaseComponent[] build() {
        return finalBuilder.create();
    }

}
