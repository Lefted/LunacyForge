package us.hemdgang.autoreward.labyconnect;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    protected void encode(final ChannelHandlerContext channelHandlerContext, final Packet packet, final ByteBuf byteBuf) throws Exception {
	final PacketBuf packetBuffer = createPacketBuf(byteBuf);
	final int id = Protocol.getProtocol().getPacketId(packet);
	// if ((id != 62 && id != 63) || this.labyConnect.getClientConnection().getCustomIp() != null) {
	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "[OUT] " + id + " " + packet.getClass().getSimpleName());
	// }
	packetBuffer.writeVarIntToBuffer(id);
	packet.write(packetBuffer);
    }

    public PacketBuf createPacketBuf(final ByteBuf byteBuf) {
	return new PacketBufOld(byteBuf);
    }
}
