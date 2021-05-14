package us.hemdgang.autoreward.labyconnect.packets;

import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketHelloPong extends Packet{

    private long a;

    public PacketHelloPong() {
    }

    public PacketHelloPong(final long a) {
	this.a = a;
    }

    @Override
    public void read(final PacketBuf buf) {
	this.a = buf.readLong();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeLong(this.a);
    }

    public int getId() {
	return 1;
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }

}
