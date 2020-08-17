package me.mattstudios.mfmsg.base.internal.component;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class BaseComponentAppender implements Appender<BaseComponent[]> {

    private final ColorComponent colorComponent;

    final List<Component> testing = new ArrayList<>();

    private final ComponentBuilder componentBuilder = new ComponentBuilder();
    private HoverEvent hoverEvent = null;
    private ClickEvent clickEvent = null;

    public BaseComponentAppender(final ColorComponent colorComponent) {
        this.colorComponent = colorComponent;
    }

    @Override
    public void append(@NotNull final String message, final boolean italic, final boolean bold, final boolean strike, final boolean underline, final boolean obfuscated) {

        //testing.add(new Component(message, "none", bold, italic, strike, underline, obfuscated));
        testing.addAll(colorComponent.colorize(message, bold, italic, strike, underline, obfuscated));
        //appendComponent(colorComponent.colorize(message));

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

    public List<Component> test() {
        final List<Component> test = new ArrayList<>(testing);
        reset();
        return test;
    }

    /**
     * Resets the {@link #componentBuilder}, {@link #hoverEvent}, and the {@link #clickEvent}
     */
    private void reset() {
        componentBuilder.getParts().clear();
        hoverEvent = null;
        clickEvent = null;
        testing.clear();
    }

    /**
     * Appends a the given {@link BaseComponent[]} into the {@link #componentBuilder}
     *
     * @param components A {@link BaseComponent[]}
     */
    private void appendComponent(@NotNull final BaseComponent[] components) {
        if (components.length == 0) return;
        componentBuilder.append(components, ComponentBuilder.FormatRetention.NONE);
    }

}
