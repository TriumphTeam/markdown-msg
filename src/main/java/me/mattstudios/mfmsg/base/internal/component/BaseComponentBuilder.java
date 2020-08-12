package me.mattstudios.mfmsg.base.internal.component;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BaseComponentBuilder implements Builder<BaseComponent[]> {

    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private HoverEvent hoverEvent = null;
    private ClickEvent clickEvent = null;

    @Override
    public void append(final @NotNull String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated) {
        appendComponent(TextComponent.fromLegacyText(message));

        // Sets each property accordingly
        componentBuilder.italic(italic);
        componentBuilder.bold(bold);
        componentBuilder.strikethrough(strike);
        componentBuilder.underlined(underline);
        componentBuilder.obfuscated(obfuscated);

        // If available sets the event
        if (hoverEvent != null) componentBuilder.event(hoverEvent);
        if (clickEvent != null) componentBuilder.event(clickEvent);
    }

    @Override
    public void setHoverEvent(final @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    @Override
    public void setClickEvent(final @Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    /**
     * Builds the component into a {@link BaseComponent[]} and resets the {@link #componentBuilder}
     *
     * @return A {@link BaseComponent[]} with all the parsed values
     */
    @NotNull
    public BaseComponent[] build() {
        final BaseComponent[] component = componentBuilder.create();
        reset();
        return component;
    }

    /**
     * Resets the {@link #componentBuilder}, {@link #hoverEvent}, and the {@link #clickEvent}
     */
    private void reset() {
        componentBuilder.getParts().clear();
        hoverEvent = null;
        clickEvent = null;
    }

    /**
     * Appends a the given {@link BaseComponent[]} into the {@link #componentBuilder}
     *
     * @param components A {@link BaseComponent[]}
     */
    private void appendComponent(@NotNull final BaseComponent[] components) {
        componentBuilder.append(components, ComponentBuilder.FormatRetention.NONE);
    }

}
