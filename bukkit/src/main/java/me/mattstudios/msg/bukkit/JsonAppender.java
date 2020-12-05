package me.mattstudios.msg.bukkit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.mattstudios.msg.base.internal.action.ClickMessageAction;
import me.mattstudios.msg.base.internal.action.HoverMessageAction;
import me.mattstudios.msg.base.internal.action.MessageAction;
import me.mattstudios.msg.base.internal.action.content.HoverContent;
import me.mattstudios.msg.base.internal.action.content.ShowItem;
import me.mattstudios.msg.base.internal.action.content.ShowText;
import me.mattstudios.msg.base.internal.color.handlers.ColorMapping;
import me.mattstudios.msg.base.internal.components.MessageNode;
import me.mattstudios.msg.base.serializer.Appender;
import me.mattstudios.msg.base.serializer.NodeScanner;
import me.mattstudios.util.ServerVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.List;

public final class JsonAppender implements Appender<String> {

    private static final Gson GSON = new Gson();
    private static final JsonParser JSON_PARSER = new JsonParser();
    private final JsonArray jsonArray = new JsonArray();

    @Override
    public void append(@NotNull final String value) {
        jsonArray.add(value);
    }

    @Override
    public void appendNode(@NotNull final String text, @Nullable final String color, final boolean bold, final boolean italic, final boolean strike, final boolean underline, final boolean obfuscated, @Nullable final List<MessageAction> actions) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", text);

        if (bold) jsonObject.addProperty("bold", true);
        if (italic) jsonObject.addProperty("italic", true);
        if (strike) jsonObject.addProperty("strikethrough", true);
        if (underline) jsonObject.addProperty("underlined", true);
        if (obfuscated) jsonObject.addProperty("obfuscated", true);

        if (color != null) {
            if (ServerVersion.CURRENT_VERSION.isColorLegacy() && color.startsWith("#")) {
                jsonObject.addProperty("color", ColorMapping.toLegacy(Color.decode(color)));
            } else {
                jsonObject.addProperty("color", color);
            }
        }

        if (actions == null || actions.isEmpty()) {
            jsonArray.add(jsonObject);
            return;
        }

        for (final MessageAction messageAction : actions) {
            if (messageAction instanceof HoverMessageAction) {
                final JsonObject hoverObject = new JsonObject();

                final HoverContent hoverContent = ((HoverMessageAction) messageAction).getHoverContent();

                if (hoverContent instanceof ShowText) {
                    hoverObject.addProperty("action", "show_text");

                    final List<MessageNode> nodes = ((ShowText) hoverContent).getNodes();

                    final JsonAppender appender = new JsonAppender();
                    NodeScanner.scan(nodes, appender);
                    final JsonArray array = appender.getJsonArray();

                    if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
                        hoverObject.add("value", array);
                    } else {
                        hoverObject.add("contents", array);
                    }
                    jsonObject.add("hoverEvent", hoverObject);
                    continue;
                }

                // TODO Show Item is WIP, Adventure version works
                final ShowItem showItem = (ShowItem) hoverContent;

                hoverObject.addProperty("action", "show_item");

                final JsonObject showItemObject = new JsonObject();
                showItemObject.addProperty("id", showItem.getId());
                showItemObject.addProperty("Count", showItem.getId());

                if (showItem.getNbt() != null) {
                    showItemObject.addProperty("tag", showItem.getNbt());
                }

                hoverObject.add("contents", showItemObject);

                jsonObject.add("hoverEvent", hoverObject);
                continue;
            }

            final ClickMessageAction clickAction = (ClickMessageAction) messageAction;

            switch (clickAction.getActionType()) {
                case ACTION_COMMAND:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "run_command"));
                    continue;

                case ACTION_SUGGEST:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "suggest_command"));
                    continue;

                case ACTION_URL:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "open_url"));
                    continue;

                case ACTION_CLIPBOARD:
                    jsonObject.add("clickEvent", getClickEvent(clickAction, "copy_to_clipboard"));
            }
        }

        jsonArray.add(jsonObject);
    }

    @NotNull
    @Override
    public String build() {
        return GSON.toJson(jsonArray);
    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    @NotNull
    private JsonObject getClickEvent(@NotNull final ClickMessageAction clickAction, @NotNull final String type) {
        final JsonObject clickObject = new JsonObject();
        clickObject.addProperty("action", type);
        clickObject.addProperty("value", clickAction.getAction());
        return clickObject;
    }

}
