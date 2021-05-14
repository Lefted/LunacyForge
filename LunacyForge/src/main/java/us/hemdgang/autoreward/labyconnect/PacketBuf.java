package us.hemdgang.autoreward.labyconnect;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import io.netty.buffer.ByteBuf;

public abstract class PacketBuf extends ByteBuf {

    protected ByteBuf buf;

    public PacketBuf(final ByteBuf buf) {
	this.buf = buf;
    }

    // public ChatUser readChatUser() {
    // final String username = this.readString();
    // final UUID uuid = this.readUUID();
    // final String statusMessage = this.readString();
    // final UserStatus status = this.readUserStatus();
    // final boolean request = this.readBoolean();
    // final String timeZone = this.readString();
    // final int contactsAmound = this.readInt();
    // final long lastOnline = this.readLong();
    // final long firstJoined = this.readLong();
    // final ServerInfo serverInfo = this.readServerInfo();
    // if (request) {
    // return new ChatRequest(new GameProfile(uuid, username));
    // }
    // return new ChatUser(new GameProfile(uuid, username), status, statusMessage, serverInfo, 0, System.currentTimeMillis(), 0L, timeZone, lastOnline,
    // firstJoined, contactsAmound, false);
    // }

    // public void writeChatUser(final ChatUser player) {
    // this.writeString(player.getGameProfile().getName());
    // this.writeUUID(player.getGameProfile().getId());
    // this.writeString(player.getStatusMessage());
    // this.writeUserStatus(player.getStatus());
    // this.writeBoolean(player.isFriendRequest());
    // this.writeString(player.getTimeZone());
    // this.writeInt(player.getContactAmount());
    // this.writeLong(player.getLastOnline());
    // this.writeLong(player.getFirstJoined());
    // this.writeServerInfo(player.getCurrentServerInfo());
    // }

    // public ServerInfo readServerInfo() {
    // final String serverIp = this.readString();
    // final int serverPort = this.readInt();
    // if (this.readBoolean()) {
    // return new ServerInfo(serverIp, serverPort, this.readString());
    // }
    // return new ServerInfo(serverIp, serverPort);
    // }
    //
    // public PacketBuf writeServerInfo(ServerInfo info) {
    // if (info == null) {
    // info = new ServerInfo("", 0);
    // }
    // this.writeString((info.getServerIp() == null) ? "" : info.getServerIp());
    // this.writeInt(info.getServerPort());
    // if (info.getSpecifiedServerName() != null) {
    // this.writeBoolean(true);
    // this.writeString(info.getSpecifiedServerName());
    // }
    // else {
    // this.writeBoolean(false);
    // }
    // return this;
    // }

    public PacketBuf writeUserStatus(int id) {
	this.writeByte(id);
	return this;
    }
    //
     public int readUserStatus() {
	 return this.readByte();
     }

    public void writeByteArray(final byte[] data) {
	this.writeInt(data.length);
	this.writeBytes(data);
    }

    public byte[] readByteArray() {
	final byte[] b = new byte[this.readInt()];
	for (int i = 0; i < b.length; ++i) {
	    b[i] = this.readByte();
	}
	return b;
    }

    public void writeEnum(final Enum<?> enume) {
	this.writeInt(enume.ordinal());
    }

    public void writeUUID(final UUID uuid) {
	this.writeString(uuid.toString());
    }

    public UUID readUUID() {
	return UUID.fromString(this.readString());
    }

    public void writeString(final String string) {
	this.writeInt(string.getBytes(StandardCharsets.UTF_8).length);
	this.writeBytes(string.getBytes(StandardCharsets.UTF_8));
    }

    public String readString() {
	final byte[] a = new byte[this.readInt()];
	for (int i = 0; i < a.length; ++i) {
	    a[i] = this.readByte();
	}
	return new String(a, StandardCharsets.UTF_8);
    }

    public static int getVarIntSize(final int input) {
	for (int var1 = 1; var1 < 5; ++var1) {
	    if ((input & -1 << var1 * 7) == 0x0) {
		return var1;
	    }
	}
	return 5;
    }

    public int readVarIntFromBuffer() {
	int var1 = 0;
	int var2 = 0;
	byte var3;
	do {
	    var3 = this.readByte();
	    var1 |= (var3 & 0x7F) << var2++ * 7;
	    if (var2 > 5) {
		throw new RuntimeException("VarInt too big");
	    }
	} while ((var3 & 0x80) == 0x80);
	return var1;
    }

    public void writeVarIntToBuffer(int input) {
	while ((input & 0xFFFFFF80) != 0x0) {
	    this.writeByte((input & 0x7F) | 0x80);
	    input >>>= 7;
	}
	this.writeByte(input);
    }
}
