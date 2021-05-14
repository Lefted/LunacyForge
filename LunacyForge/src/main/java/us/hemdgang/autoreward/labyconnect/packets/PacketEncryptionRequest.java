package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketEncryptionRequest extends Packet {

    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    public PacketEncryptionRequest(final String serverId, final byte[] publicKey, final byte[] verifyToken) {
	this.serverId = serverId;
	this.publicKey = publicKey;
	this.verifyToken = verifyToken;
    }

    public PacketEncryptionRequest() {
    }

    public byte[] getPublicKey() {
	return this.publicKey;
    }

    public String getServerId() {
	return this.serverId;
    }

    public byte[] getVerifyToken() {
	return this.verifyToken;
    }

    @Override
    public void read(final PacketBuf buf) {
	this.serverId = buf.readString();
	this.publicKey = buf.readByteArray();
	this.verifyToken = buf.readByteArray();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeString(this.serverId);
	buf.writeByteArray(this.publicKey);
	buf.writeByteArray(this.verifyToken);
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }
}
