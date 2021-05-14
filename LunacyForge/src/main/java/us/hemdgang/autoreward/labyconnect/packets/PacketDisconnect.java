package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketDisconnect extends Packet{

    private String reason;

    public PacketDisconnect() {
    }

    public PacketDisconnect(final String reason) {
	this.reason = reason;
    }

    @Override
    public void read(final PacketBuf buf) {
	this.reason = buf.readString();
    }

    @Override
    public void write(final PacketBuf buf) {
	if (this.getReason() == null) {
	    buf.writeString("Client Error");
	    return;
	}
	buf.writeString(this.getReason());
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }

    public String getReason() {
	return this.reason;
    }
}
