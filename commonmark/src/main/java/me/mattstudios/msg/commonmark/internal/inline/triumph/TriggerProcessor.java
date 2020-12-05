package me.mattstudios.msg.commonmark.internal.inline.triumph;

import me.mattstudios.msg.commonmark.internal.inline.ParsedInline;
import me.mattstudios.msg.commonmark.internal.inline.Scanner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TriggerProcessor {

    char getTriggerCharacter();

    @Nullable
    ParsedInline parse(@NotNull final Scanner scanner);

}
