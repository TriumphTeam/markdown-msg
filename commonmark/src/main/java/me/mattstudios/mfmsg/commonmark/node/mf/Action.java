package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Action extends Node {

    @NotNull
    private final Map<String, String> actions;

    public Action(@NotNull final Map<String, String> actions) {
        this.actions = actions;
    }

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

    @NotNull
    public Map<String, String> getActions() {
        return actions;
    }
}
