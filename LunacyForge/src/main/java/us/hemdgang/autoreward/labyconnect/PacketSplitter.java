package us.hemdgang.autoreward.labyconnect;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketSplitter extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buffer, ByteBuf byteBuf) throws Exception {
	final int var4 = buffer.readableBytes();
	final int var5 = PacketBuf.getVarIntSize(var4);
	if (var5 > 3) {
	    throw new IllegalArgumentException("unable to fit " + var4 + " into " + 3);
	}
	final PacketBuf packetBuffer = createPacketBuf(byteBuf);
	packetBuffer.ensureWritable(var5 + var4);
	packetBuffer.writeVarIntToBuffer(var4);
	packetBuffer.writeBytes(buffer, buffer.readerIndex(), var4);
    }

    public PacketBuf createPacketBuf(final ByteBuf byteBuf) {
	return new PacketBufOld(byteBuf);
    }
}
