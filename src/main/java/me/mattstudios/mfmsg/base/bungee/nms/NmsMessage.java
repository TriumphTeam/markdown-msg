package me.mattstudios.mfmsg.base.bungee.nms;

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
    private static final Class<?> CHAT_PACKET;

    private static Class CHAT_TYPE;

    private static final MethodHandle CHAT_SERIALIZER, PLAYER_HANDLE, SEND_PACKET;
    private static final Field PLAYER_CONNECTION;

    static {
        try {
            CHAT_COMPONENT = getNmsClass("IChatBaseComponent");
            CHAT_SERIALIZER = LOOKUP.findStatic(getNmsClass("IChatBaseComponent$ChatSerializer"), "a", MethodType.methodType(getNmsClass("IChatMutableComponent"), String.class));

            if (!ServerVersion.CURRENT_VERSION.isLegacy()) CHAT_TYPE = getNmsClass("ChatMessageType");

            CHAT_PACKET = getNmsClass("PacketPlayOutChat");
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
            if (ServerVersion.CURRENT_VERSION == ServerVersion.V1_8_R3) {
                packet = LOOKUP.findConstructor(CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT)).invokeWithArguments(CHAT_SERIALIZER.invokeWithArguments(message));
            } else if (ServerVersion.CURRENT_VERSION.isColorLegacy()) {
                packet = LOOKUP.findConstructor(CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, CHAT_TYPE)).invokeWithArguments(CHAT_SERIALIZER.invokeWithArguments(message), Enum.valueOf(CHAT_TYPE, "CHAT"));
            } else {
                packet = LOOKUP.findConstructor(CHAT_PACKET, MethodType.methodType(void.class, CHAT_COMPONENT, CHAT_TYPE, UUID.class)).invokeWithArguments(CHAT_SERIALIZER.invoke(message), Enum.valueOf(CHAT_TYPE, "CHAT"), player.getUniqueId());
            }

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
