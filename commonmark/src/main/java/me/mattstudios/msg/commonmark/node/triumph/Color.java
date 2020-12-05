package me.mattstudios.msg.commonmark.node.triumph;

import me.mattstudios.msg.commonmark.node.Node;
import me.mattstudios.msg.commonmark.node.Visitor;
import org.jetbrains.annotations.NotNull;

public class Color extends Node {

    @NotNull
    private String literal;
    private final boolean legacy;

    public Color(@NotNull final String literal) {
        this(literal, false);
    }

    public Color(@NotNull final String literal, final boolean legacy) {
        this.literal = literal;
        this.legacy = legacy;
    }

    @Override
    public void accept(@NotNull final Visitor visitor) {
        visitor.visit(this);
    }

    @NotNull
    public String getColor() {
        return literal;
    }

    public void setLiteral(@NotNull final String literal) {
        this.literal = literal;
    }

    public boolean isLegacy() {
        return legacy;
    }

}
