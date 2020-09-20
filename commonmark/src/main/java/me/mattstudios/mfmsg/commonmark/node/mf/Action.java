package me.mattstudios.mfmsg.commonmark.node.mf;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Action extends Node {

    private Map<String, String> actions;

    public Action(@NotNull Map<String, String> actions) {
        this.actions = actions;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    public Map<String, String> getActions() {
        return actions;
    }
}
