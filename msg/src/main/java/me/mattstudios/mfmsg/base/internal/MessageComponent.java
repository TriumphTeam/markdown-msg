package me.mattstudios.mfmsg.base.internal;

import me.mattstudios.mfmsg.base.internal.component.MessageNode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Message component that will be used for all the future ones, currently only Bukkit supported
 */
public interface MessageComponent {

    /**
     * Sends the parsed JSON message to the player
     *
     * @param player The player to receive the message
     */
    void sendMessage(@NotNull final Player player);

    /**
     * Sends the JSON title to the player
     *
     * @param player  The player to receive it
     * @param fadeIn  The fade in time in ticks
     * @param stay    The stay time in ticks
     * @param fadeOut The fade out time in ticks
     */
    void sendTitle(@NotNull final Player player, final int fadeIn, final int stay, final int fadeOut);

    /**
     * Sends the JSON subtitle to the player
     *
     * @param player  The player to receive it
     * @param fadeIn  The fade in time in ticks
     * @param stay    The stay time in ticks
     * @param fadeOut The fade out time in ticks
     */
    void sendSubTitle(@NotNull final Player player, final int fadeIn, final int stay, final int fadeOut);

    /**
     * Sends the JSON actionbar to the player
     *
     * @param player  The player to receive it
     * @param fadeIn  The fade in time in ticks
     * @param stay    The stay time in ticks
     * @param fadeOut The fade out time in ticks
     */
    void sendActionBar(@NotNull final Player player, final int fadeIn, final int stay, final int fadeOut);

    /**
     * Turns the JSON message into string to be used in Items etc
     *
     * @return The JSON as string
     */
    @NotNull
    @Override
    String toString();

    /**
     * Gets the raw JSON created
     *
     * @return The raw JSON message
     */
    @NotNull
    String toJson();

    @NotNull
    List<MessageNode> getMessageLines();

}
