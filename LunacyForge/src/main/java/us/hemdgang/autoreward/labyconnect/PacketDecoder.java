package us.hemdgang.autoreward.labyconnect;

import java.io.IOException;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> objects) throws Exception {
	final PacketBuf packetBuffer = createPacketBuf(byteBuf);
	if (packetBuffer.readableBytes() < 1) {
	    return;
	}
	final int id = packetBuffer.readVarIntFromBuffer();
	final Packet packet = Protocol.getProtocol().getPacket(id);
	// if ((id != 62 && id != 63) || this.labyConnect.getClientConnection().getCustomIp() != null) {
	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "[IN] " + id + " " + packet.getClass().getSimpleName());
	// }
	packet.read(packetBuffer);
	if (packetBuffer.readableBytes() > 0) {
	    throw new IOException("Packet  (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetBuffer.readableBytes()
		+ " bytes extra whilst reading packet " + packet);
	}
	objects.add(packet);
    }

    public PacketBuf createPacketBuf(final ByteBuf byteBuf) {
	return new PacketBufOld(byteBuf);
    }
}
