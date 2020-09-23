package me.mattstudios.mfmsg.bukkit;

import me.mattstudios.mfmsg.base.internal.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Class for handling packet sending
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class NmsMessage {

    private static final String PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName();
    private static final String NMS_VERSION = PACKAGE_NAME.substring(PACKAGE_NAME.lastIndexOf('.') + 1);

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    protected static final Version CURRENT_VERSION = Version.getByNmsName(NMS_VERSION);

    private static final Class<?> CHAT_COMPONENT, CHAT_PACKET, TITLE_PACKET;

    private static Class CHAT_TYPE;
    private static final Class TITLE_TYPE;

    private static final MethodHandle CHAT_SERIALIZER, PLAYER_HANDLE, SEND_PACKET;
    private static final Field PLAYER_CONNECTION;

    // Initializes all the reflection stuff
    static {
        try {
            CHAT_COMPONENT = getNmsClass("IChatBaseComponent");

            // Because of all the NMS changes throughout the versions
            if (CURRENT_VERSION.isOlderThan(Version.V1_12_R1)) {
                CHAT_SERIALIZER = LOOKUP.findStatic(getNmsClass("IChatBaseComponent$ChatSerializer"), "a", MethodType.methodType(CHAT_COMPONENT, String.class));
            } else if (CURRENT_VERSION.isLegacy()) {
                CHAT_SERIALIZER = LOOKUP.findStatic(getNmsClass("IChatBaseComponent$ChatSerializer"), "a", MethodType.methodType(CHAT_COMPONENT, String.class));
                CHAT_TYPE = getNmsClass("ChatMessageType");
            } else {
                CHAT_SERIALIZER = LOOKUP.findStatic(getNmsClass("IChatBaseComponent$ChatSerializer"), "a", MethodType.methodType(getNmsClass("IChatMutableComponent"), String.class));
                CHAT_TYPE = getNmsClass("ChatMessageType");
            }

            TITLE_TYPE = getNmsClass("PacketPlayOutTitle$EnumTitleAction");

            CHAT_PACKET = getNmsClass("PacketPlayOutChat");
            TITLE_PACKET = getNmsClass("PacketPlayOutTitle");

            final Class<?> entityPlayerClass = getNmsClass("EntityPlayer");
            PLAYER_HANDLE = LOOKUP.findVirtual(getCraftClass("entity.CraftPlayer"), "getHandle", MethodType.methodType(entityPlayerClass));

            PLAYER_CONNECTION = entityPlayerClass.getField("playerConnection");
            final Class<?> packetClass = getNmsClass("Packet");
            final Class<?> playerConnectionClass = PLAYER_CONNECTION.getType();

            SEND_PACKET = LOOKUP.findVirtual(playerConnectionClass, "sendPacket", MethodType.methodType(Void.TYPE, packetClass));

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the message to the player using packets
     *
     * @param player  The {@link Player}
     * @param message The json message to send
     */
    public static void sendMessage(@NotNull final Player player, @NotNull final String message) {
        try {
            final Object packet;
            // For versions from 1.8 - 1.11
            if (CURRENT_VERSION.isOlderThan(Version.V1_12_R1)) {
                packet = LOOKUP.findConstructor(
                        CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT)
                ).invokeWithArguments(
                        CHAT_SERIALIZER.invokeWithArguments(message)
                );

                sendPacket(player, packet);
                return;
            }

            // For versions 1.12 - 1.15
            if (CURRENT_VERSION.isColorLegacy()) {
                packet = LOOKUP.findConstructor(
                        CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, CHAT_TYPE)
                ).invokeWithArguments(
                        CHAT_SERIALIZER.invokeWithArguments(message), Enum.valueOf(CHAT_TYPE, "CHAT")
                );

                sendPacket(player, packet);
                return;
            }

            // For 1.16+
            packet = LOOKUP.findConstructor(
                    CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, CHAT_TYPE, UUID.class)
            ).invokeWithArguments(
                    CHAT_SERIALIZER.invoke(message), Enum.valueOf(CHAT_TYPE, "CHAT"), player.getUniqueId()
            );

            sendPacket(player, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a title packet to the {@link Player}
     *
     * @param player    The {@link Player} to receive the title
     * @param message   The json message
     * @param titleType The type of title to send, example: TITLE, SUBTITLE, ACTIONBAR
     * @param fadeIn    {@link Integer} in ticks of fade in time
     * @param stay      {@link Integer} in ticks of stay time
     * @param fadeOut   {@link Integer} in ticks of fade out time
     */
    public static void sendTitle(@NotNull final Player player, @NotNull final String message, @NotNull final TitleType titleType, final int fadeIn, final int stay, final int fadeOut) {
        try {
            final Object packet;
            // For versions older than 1.12 and only for Actionbar since it used to be in the ChatPacket
            if (CURRENT_VERSION.isOlderThan(Version.V1_12_R1) && titleType == TitleType.ACTIONBAR) {
                packet = LOOKUP.findConstructor(
                        CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, byte.class)
                ).invokeWithArguments(
                        CHAT_SERIALIZER.invoke(message), (byte) 2
                );

                sendPacket(player, packet);
                return;
            }

            // For All 1.12+ versions and titles etc for 1.8+
            packet = LOOKUP.findConstructor(
                    TITLE_PACKET, MethodType.methodType(void.class, TITLE_TYPE, CHAT_COMPONENT, int.class, int.class, int.class)
            ).invokeWithArguments(
                    Enum.valueOf(TITLE_TYPE, titleType.name()), CHAT_SERIALIZER.invoke(message), fadeIn, stay, fadeOut
            );

            sendPacket(player, packet);

            // Required for subtitle to send
            if (titleType == TitleType.SUBTITLE) {
                final Object titlePacket = LOOKUP.findConstructor(
                        TITLE_PACKET, MethodType.methodType(void.class, TITLE_TYPE, CHAT_COMPONENT, int.class, int.class, int.class)
                ).invokeWithArguments(
                        Enum.valueOf(TITLE_TYPE, TitleType.TITLE.name()), null, fadeIn, stay, fadeOut
                );

                sendPacket(player, titlePacket);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the packet to the {@link Player}
     *
     * @param player The {@link Player} to receive the packet
     * @param packet The packet {@link Object}
     * @throws Throwable Throws a throwable in case something goes wrong
     */
    private static void sendPacket(@NotNull final Player player, @NotNull final Object packet) throws Throwable {
        Object playerConnection = PLAYER_CONNECTION.get(PLAYER_HANDLE.invoke(player));
        SEND_PACKET.invoke(playerConnection, packet);
    }

    /**
     * Gets the NMS class needed
     *
     * @param path The path to the NMS {@link Class} to get
     * @return Returns the correct NMS {@link Class} for the path given
     * @throws ClassNotFoundException If the class isn't found throws exception
     */
    @NotNull
    private static Class<?> getNmsClass(@NotNull final String path) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + NMS_VERSION + "." + path);
    }

    /**
     * Gets the Craft class needed
     *
     * @param path The path to the Craft {@link Class} to get
     * @return Returns the correct Craft {@link Class} for the path given
     * @throws ClassNotFoundException If the class isn't found throws exception
     */
    @NotNull
    private static Class<?> getCraftClass(@NotNull final String path) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + "." + path);
    }

    public enum TitleType {
        ACTIONBAR,
        TITLE,
        SUBTITLE
    }

}
