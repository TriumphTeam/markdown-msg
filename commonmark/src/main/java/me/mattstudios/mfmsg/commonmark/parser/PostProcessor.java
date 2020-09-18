package me.mattstudios.mfmsg.commonmark.parser;

import me.mattstudios.mfmsg.commonmark.node.Node;

public interface PostProcessor {

    /**
     * @param node the node to post-process
     * @return the result of post-processing, may be a modified {@code node} argument
     */
    Node process(Node node);

}
