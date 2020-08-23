package me.mattstudios.mfmsg.base.bukkit.nms;

import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.UUID;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class NmsMessage {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private static final Class<?> CHAT_COMPONENT;
    private static final Class<?> CHAT_PACKET, TITLE_PACKET;

    private static Class CHAT_TYPE, TITLE_TYPE;

    private static final MethodHandle CHAT_SERIALIZER, PLAYER_HANDLE, SEND_PACKET;
    private static final Field PLAYER_CONNECTION;

    static {
        try {
            CHAT_COMPONENT = getNmsClass("IChatBaseComponent");

            if (ServerVersion.CURRENT_VERSION.isOlderThan(ServerVersion.V1_12_R1)) {
                CHAT_SERIALIZER = LOOKUP.findStatic(getNmsClass("IChatBaseComponent$ChatSerializer"), "a", MethodType.methodType(CHAT_COMPONENT, String.class));
            } else if (ServerVersion.CURRENT_VERSION.isLegacy()) {
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

    public static void sendMessage(final Player player, final String message) {
        try {
            final Object packet;
            if (ServerVersion.CURRENT_VERSION.isOlderThan(ServerVersion.V1_12_R1)) {
                packet = LOOKUP.findConstructor(
                        CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT)
                ).invokeWithArguments(
                        CHAT_SERIALIZER.invokeWithArguments(message)
                );

                sendPacket(player, packet);
                return;
            }

            if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
                packet = LOOKUP.findConstructor(
                        CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, CHAT_TYPE)
                ).invokeWithArguments(
                        CHAT_SERIALIZER.invokeWithArguments(message), Enum.valueOf(CHAT_TYPE, "CHAT")
                );

                sendPacket(player, packet);
                return;
            }

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

    public static void sendTitle(final Player player, final String message, final String titleType, final int fadeIn, final int stay, final int fadeOut) {
        try {
            final Object packet;
            if (ServerVersion.CURRENT_VERSION.isOlderThan(ServerVersion.V1_12_R1) && titleType.equals("ACTIONBAR")) {
                packet = LOOKUP.findConstructor(
                        CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, byte.class)
                ).invokeWithArguments(
                        CHAT_SERIALIZER.invoke(message), (byte) 2
                );

                sendPacket(player, packet);
                return;
            }

            packet =
                    LOOKUP.findConstructor(
                            TITLE_PACKET, MethodType.methodType(void.class, TITLE_TYPE, CHAT_COMPONENT, int.class, int.class, int.class)
                    ).invokeWithArguments(
                            Enum.valueOf(TITLE_TYPE, titleType), CHAT_SERIALIZER.invoke(message), fadeIn, stay, fadeOut
                    );

            sendPacket(player, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(final Player player, final Object packet) throws Throwable {
        Object playerConnection = PLAYER_CONNECTION.get(PLAYER_HANDLE.invoke(player));
        SEND_PACKET.invoke(playerConnection, packet);
    }

    private static Class<?> getNmsClass(final String clazz) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + ServerVersion.NMS_VERSION + "." + clazz);
    }

    private static Class<?> getCraftClass(final String clazz) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + ServerVersion.NMS_VERSION + "." + clazz);
    }

}
