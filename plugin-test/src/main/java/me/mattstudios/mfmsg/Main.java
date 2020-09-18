package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlRenderer;

public final class Main {

    public static void main(String[] args) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(" This is *Sparta* ");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        System.out.println("|" + renderer.render(document).replace("\n", "") + "|");
    }

}
