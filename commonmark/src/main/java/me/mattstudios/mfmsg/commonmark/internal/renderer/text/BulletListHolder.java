package me.mattstudios.mfmsg.commonmark.internal.renderer.text;

import me.mattstudios.mfmsg.commonmark.node.BulletList;

public class BulletListHolder extends ListHolder {
    private final char marker;

    public BulletListHolder(ListHolder parent, BulletList list) {
        super(parent);
        marker = list.getBulletMarker();
    }

    public char getMarker() {
        return marker;
    }
}
