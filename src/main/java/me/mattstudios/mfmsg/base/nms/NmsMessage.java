package me.mattstudios.mfmsg.base.nms;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@SuppressWarnings({"unchecked", "JavaReflectionInvocation", "rawtypes"})
public final class NmsMessage {

    private static final Class<?> CHAT_COMPONENT;
    private static final Class<?> CHAT_PACKET;

    private static Class CHAT_TYPE;

    private static final Method CHAT_SERIALIZER_METHOD, PLAYER_HANDLE, SEND_PACKET;
    private static final Field PLAYER_CONNECTION;

    static {
        try {
            CHAT_SERIALIZER_METHOD = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);

            CHAT_COMPONENT = getNmsClass("IChatBaseComponent");

            if (!ServerVersion.CURRENT_VERSION.isLegacy()) CHAT_TYPE = getNmsClass("ChatMessageType");

            CHAT_PACKET = getNmsClass("PacketPlayOutChat");
            PLAYER_HANDLE = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
            final Class<?> entityPlayerClass = PLAYER_HANDLE.getReturnType();
            PLAYER_CONNECTION = entityPlayerClass.getField("playerConnection");
            final Class<?> packetClass = getNmsClass("Packet");
            final Class<?> playerConnectionClass = PLAYER_CONNECTION.getType();
            SEND_PACKET = playerConnectionClass.getMethod("sendPacket", packetClass);

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessage(final Player player, final String message) {
        try {
            final Object packet;
            if (ServerVersion.CURRENT_VERSION == ServerVersion.V1_8_R3) {
                packet = CHAT_PACKET.getConstructor(CHAT_COMPONENT).newInstance(CHAT_SERIALIZER_METHOD.invoke(null, message));
            } else if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
                packet = CHAT_PACKET.getConstructor(CHAT_COMPONENT, CHAT_TYPE).newInstance(CHAT_SERIALIZER_METHOD.invoke(null, message), Enum.valueOf(CHAT_TYPE, "CHAT"));
            } else {
                packet = CHAT_PACKET.getConstructor(CHAT_COMPONENT, CHAT_TYPE, UUID.class).newInstance(CHAT_SERIALIZER_METHOD.invoke(null, message), Enum.valueOf(CHAT_TYPE, "CHAT"), player.getUniqueId());
            }

            sendPacket(player, packet);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(final Player player, final Object packet) throws InvocationTargetException, IllegalAccessException {
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
