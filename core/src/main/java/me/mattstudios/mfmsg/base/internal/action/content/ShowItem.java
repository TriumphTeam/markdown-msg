package me.mattstudios.mfmsg.base.internal.action.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ShowItem implements HoverContent {

    @NotNull
    private final String id;

    private final int count;

    @Nullable
    private final String nbt;

    public ShowItem(@NotNull final String id, final int count, @Nullable final String nbt) {
        this.id = id;
        this.count = count;
        this.nbt = nbt;
    }

    public ShowItem(@NotNull final String id, final int count) {
        this(id, count, null);
    }

    public ShowItem(@NotNull final String id) {
        this(id, 1, null);
    }

    @NotNull
    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    @Nullable
    public String getNbt() {
        return nbt;
    }
}
