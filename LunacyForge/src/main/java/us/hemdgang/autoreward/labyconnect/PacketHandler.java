package us.hemdgang.autoreward.labyconnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import us.hemdgang.autoreward.labyconnect.packets.PacketAddonMessage;
import us.hemdgang.autoreward.labyconnect.packets.PacketDisconnect;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptionRequest;
import us.hemdgang.autoreward.labyconnect.packets.PacketEncryptionResponse;
import us.hemdgang.autoreward.labyconnect.packets.PacketHelloPing;
import us.hemdgang.autoreward.labyconnect.packets.PacketHelloPong;
import us.hemdgang.autoreward.labyconnect.packets.PacketKick;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginData;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginOptions;
import us.hemdgang.autoreward.labyconnect.packets.PacketLoginVersion;
import us.hemdgang.autoreward.labyconnect.packets.PacketServerMessage;

public abstract class PacketHandler extends SimpleChannelInboundHandler<Object>{

    protected void channelRead0(final ChannelHandlerContext ctx, Object packet) throws Exception {
	this.handlePacket((Packet) packet);
    }
    
    protected void handlePacket(final Packet packet) {
	packet.handle(this);
    }
    
    public abstract void handle(final PacketHelloPing p0);
    
    public abstract void handle(final PacketHelloPong p0);
    
    public abstract void handle(final PacketNotAllowed p0);
    
    public abstract void handle(final PacketServerMessage p0);
    
    public abstract void handle (final PacketDisconnect packet);
    
    public abstract void handle (final PacketAddonMessage packet);

    public abstract void handle(PacketLoginData packetLoginData);

    public abstract void handle(PacketLoginOptions packetLoginOptions);

    public abstract void handle(PacketLoginVersion packetLoginVersion);

    public abstract void handle(PacketEncryptionRequest packetEncryptionRequest);

    public abstract void handle(PacketEncryptionResponse packetEncryptionResponse);

    public abstract void handle(PacketKick packetKick);
}
