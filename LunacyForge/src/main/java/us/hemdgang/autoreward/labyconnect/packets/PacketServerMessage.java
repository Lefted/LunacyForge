package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketServerMessage extends Packet {

    private String message;

    public PacketServerMessage(final String message) {
	this.message = message;
    }

    public PacketServerMessage() {
    }

    @Override
    public void read(final PacketBuf buf) {
	this.message = buf.readString();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeString(this.message);
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }

    public String getMessage() {
	return this.message;
    }
}
