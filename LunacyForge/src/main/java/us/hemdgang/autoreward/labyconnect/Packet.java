package us.hemdgang.autoreward.labyconnect;

public abstract class Packet {


    public abstract void read(final PacketBuf p);
    
    public abstract void write(final PacketBuf p);
    
    public abstract void handle(final PacketHandler p);
}
