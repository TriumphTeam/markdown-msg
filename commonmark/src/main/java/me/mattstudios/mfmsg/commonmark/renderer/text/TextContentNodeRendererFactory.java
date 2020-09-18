package me.mattstudios.mfmsg.commonmark.renderer.text;

import me.mattstudios.mfmsg.commonmark.renderer.NodeRenderer;

/**
 * Factory for instantiating new node renderers when rendering is done.
 */
public interface TextContentNodeRendererFactory {

    /**
     * Create a new node renderer for the specified rendering context.
     *
     * @param context the context for rendering (normally passed on to the node renderer)
     * @return a node renderer
     */
    NodeRenderer create(TextContentNodeRendererContext context);
}
