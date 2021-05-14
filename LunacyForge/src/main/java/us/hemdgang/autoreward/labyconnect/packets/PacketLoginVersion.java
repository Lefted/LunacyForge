package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketLoginVersion extends Packet {

    private int versionId;
    private String versionName;
    private String updateLink;

    public PacketLoginVersion(final int internalVersion, final String externalVersion) {
	this.versionId = internalVersion;
	this.versionName = externalVersion;
    }

    public PacketLoginVersion() {
    }

    @Override
    public void read(final PacketBuf buf) {
	this.versionId = buf.readInt();
	this.versionName = buf.readString();
	this.updateLink = buf.readString();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeInt(this.versionId);
	buf.writeString(this.versionName);
	buf.writeString("");
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }

    public String getVersionName() {
	return this.versionName;
    }

    public int getVersionID() {
	return this.versionId;
    }

    public String getUpdateLink() {
	return this.updateLink;
    }
}
