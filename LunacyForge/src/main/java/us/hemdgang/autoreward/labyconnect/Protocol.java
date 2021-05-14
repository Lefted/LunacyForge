package us.hemdgang.autoreward.labyconnect;

import java.util.HashMap;
import java.util.Map;

import us.hemdgang.autoreward.labyconnect.packets.PacketAddonMessage;
import us.hemdgang.autoreward.labyconnect.packets.PacketDisconnect;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptionRequest;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptionResponse;
import us.hemdgang.autoreward.labyconnect.packets.PacketHelloPing;
import us.hemdgang.autoreward.labyconnect.packets.PacketHelloPong;
import us.hemdgang.autoreward.labyconnect.packets.PacketKick;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginData;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginOptions;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginStart;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginVersion;
import us.hemdgang.autoreward.labyconnect.packets.PacketServerMessage;

public class Protocol {
    private static final Protocol INSTANCE;
    private Map<Class<? extends Packet>, EnumConnectionState> protocol;
    private Map<Integer, Class<? extends Packet>> packets;

    public static Protocol getProtocol() {
	return Protocol.INSTANCE;
    }

    public Protocol() {
	this.protocol = new HashMap<Class<? extends Packet>, EnumConnectionState>();
	this.packets = new HashMap<Integer, Class<? extends Packet>>();
	this.register(0, PacketHelloPing.class, EnumConnectionState.HELLO);
	this.register(1, PacketHelloPong.class, EnumConnectionState.HELLO);
	 this.register(2, PacketLoginStart.class, EnumConnectionState.LOGIN); // seems to be used nowhere
	 this.register(3, PacketLoginData.class, EnumConnectionState.LOGIN);
	// this.register(4, PacketLoginFriend.class, EnumConnectionState.LOGIN);
	// this.register(5, PacketLoginRequest.class, EnumConnectionState.LOGIN);
	 this.register(6, PacketLoginOptions.class, EnumConnectionState.LOGIN);
	// this.register(7, PacketLoginComplete.class, EnumConnectionState.LOGIN);
	// this.register(8, PacketLoginTime.class, EnumConnectionState.LOGIN);
	 this.register(9, PacketLoginVersion.class, EnumConnectionState.LOGIN);
	 this.register(10, PacketEncryptionRequest.class, EnumConnectionState.LOGIN);
	 this.register(11, PacketEncryptionResponse.class, EnumConnectionState.LOGIN);
	// this.register(14, PacketPlayPlayerOnline.class, EnumConnectionState.PLAY);
	// this.register(16, PacketPlayRequestAddFriend.class, EnumConnectionState.PLAY);
	// this.register(17, PacketPlayRequestAddFriendResponse.class, EnumConnectionState.PLAY);
	// this.register(18, PacketPlayRequestRemove.class, EnumConnectionState.PLAY);
	// this.register(19, PacketPlayDenyFriendRequest.class, EnumConnectionState.PLAY);
	// this.register(20, PacketPlayFriendRemove.class, EnumConnectionState.PLAY);
	// this.register(21, PacketPlayChangeOptions.class, EnumConnectionState.PLAY);
	// this.register(22, PacketPlayServerStatus.class, EnumConnectionState.PLAY);
	// this.register(23, PacketPlayFriendStatus.class, EnumConnectionState.PLAY);
	// this.register(24, PacketPlayFriendPlayingOn.class, EnumConnectionState.PLAY);
	// this.register(25, PacketPlayTyping.class, EnumConnectionState.PLAY);
	// this.register(26, PacketMojangStatus.class, EnumConnectionState.PLAY);
	// this.register(27, PacketActionPlay.class, EnumConnectionState.PLAY);
	// this.register(28, PacketActionPlayResponse.class, EnumConnectionState.PLAY);
	// this.register(29, PacketActionRequest.class, EnumConnectionState.PLAY);
	// this.register(30, PacketActionRequestResponse.class, EnumConnectionState.PLAY);
	// this.register(31, PacketUpdateCosmetics.class, EnumConnectionState.PLAY);
	this.register(32, PacketAddonMessage.class, EnumConnectionState.PLAY);
	// this.register(33, PacketUserBadge.class, EnumConnectionState.PLAY);
	// this.register(34, PacketAddonDevelopment.class, EnumConnectionState.PLAY);
	this.register(60, PacketDisconnect.class, EnumConnectionState.ALL);
	 this.register(61, PacketKick.class, EnumConnectionState.ALL);
	// this.register(62, PacketPing.class, EnumConnectionState.ALL);
	// this.register(63, PacketPong.class, EnumConnectionState.ALL);
	this.register(64, PacketServerMessage.class, EnumConnectionState.ALL);
	// this.register(65, PacketMessage.class, EnumConnectionState.ALL);
	// this.register(66, PacketBanned.class, EnumConnectionState.ALL);
	this.register(66, PacketNotAllowed.class, EnumConnectionState.ALL);
	// this.register(67, PacketChatVisibilityChange.class, EnumConnectionState.ALL);
	// this.register(68, PacketPlayServerStatusUpdate.class, EnumConnectionState.PLAY);
    }

    public Map<Integer, Class<? extends Packet>> getPackets() {
	return this.packets;
    }

    private final void register(final int id, final Class<? extends Packet> clazz, final EnumConnectionState state) {
	try {
	    clazz.newInstance();
	    this.packets.put(id, clazz);
	    this.protocol.put(clazz, state);
	} catch (Exception e) {
	    System.out.println(String.format("Class %s does not contain a default constructor", clazz.getSimpleName()));
	    e.printStackTrace();
	}
    }

    public Packet getPacket(final int id) throws IllegalAccessException, InstantiationException {
	if (!this.packets.containsKey(id)) {
	    throw new RuntimeException("Packet with id " + id + " is not registered.");
	}
	return (Packet) this.packets.get(id).newInstance();
    }

    public int getPacketId(final Packet packet) {
	for (final Map.Entry<Integer, Class<? extends Packet>> entry : this.packets.entrySet()) {
	    final Class<? extends Packet> clazz = entry.getValue();
	    if (clazz.isInstance(packet)) {
		return entry.getKey();
	    }
	}
	throw new RuntimeException("Packet " + packet + " is not registered.");
    }

    static {
	INSTANCE = new Protocol();
    }
}