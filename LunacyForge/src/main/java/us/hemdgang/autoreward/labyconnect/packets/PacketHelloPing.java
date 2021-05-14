package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketHelloPing extends Packet {

    private long a;

    public PacketHelloPing() {
    }

    public PacketHelloPing(final long a) {
    }

    @Override
    public void read(final PacketBuf buf) {
	this.a = buf.readLong();
	buf.readInt();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeLong(this.a);
	buf.writeInt(24);
    }

    public int getId() {
	return 0;
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }
}
