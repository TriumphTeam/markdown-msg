package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.base.Message;
import me.mattstudios.mfmsg.commonmark.node.Node;
import me.mattstudios.mfmsg.commonmark.parser.Parser;
import me.mattstudios.mfmsg.commonmark.renderer.html.HtmlRenderer;

public final class Main {

    public static void main(String[] args) {
        final Message message = Message.create();

        //final MessageComponent messageComponent = message.parse(" **Hello** ever__yone__! ");

        Parser parser = Parser.builder().build();
        Node document = parser.parse(" **Hello** everyone! ");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        System.out.println(renderer.render(document));
    }

}
