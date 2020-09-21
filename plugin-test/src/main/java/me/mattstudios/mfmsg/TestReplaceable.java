package me.mattstudios.mfmsg;

import me.mattstudios.mfmsg.base.internal.components.MessageNode;
import me.mattstudios.mfmsg.base.internal.extensions.ReplaceableExtension;
import org.jetbrains.annotations.NotNull;

public final class TestReplaceable extends ReplaceableExtension {

    public TestReplaceable() {
        super('{', '}', 1);
    }

    @NotNull
    @Override
    public MessageNode getNode(@NotNull final String literal) {
        return null;
    }
}
