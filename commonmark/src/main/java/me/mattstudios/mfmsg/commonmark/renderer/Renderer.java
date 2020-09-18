package me.mattstudios.mfmsg.commonmark.renderer;

import me.mattstudios.mfmsg.commonmark.node.Node;

public interface Renderer {

    /**
     * Render the tree of nodes to output.
     *
     * @param node the root node
     * @param output output for rendering
     */
    void render(Node node, Appendable output);

    /**
     * Render the tree of nodes to string.
     *
     * @param node the root node
     * @return the rendered string
     */
    String render(Node node);
}