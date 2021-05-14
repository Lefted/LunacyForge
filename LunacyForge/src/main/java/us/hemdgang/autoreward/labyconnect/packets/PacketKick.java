package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketKick extends Packet {
    
    private String cause;

    public PacketKick(final String cause) {
	this.cause = cause;
    }

    public PacketKick() {
    }

    @Override
    public void read(final PacketBuf buf) {
	this.cause = buf.readString();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeString(this.getReason());
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }

    public String getReason() {
	return this.cause;
    }
}
