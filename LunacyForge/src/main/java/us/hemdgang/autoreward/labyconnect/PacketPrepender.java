package us.hemdgang.autoreward.labyconnect;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketPrepender extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> objects) throws Exception {
	buffer.markReaderIndex();
        final byte[] a = new byte[3];
        for (int i = 0; i < a.length; ++i) {
            if (!buffer.isReadable()) {
                buffer.resetReaderIndex();
                return;
            }
            a[i] = buffer.readByte();
            if (a[i] >= 0) {
                final PacketBuf buf = createPacketBuf(Unpooled.wrappedBuffer(a));
                try {
                    final int varInt = buf.readVarIntFromBuffer();
                    if (buffer.readableBytes() < varInt) {
                        buffer.resetReaderIndex();
                        return;
                    }
                    objects.add(buffer.readBytes(varInt));
                }
                finally {
                    buf.release();
                }
                return;
            }
        }
        throw new RuntimeException("length wider than 21-bit");
    }

    public PacketBuf createPacketBuf(final ByteBuf byteBuf) {
	return new PacketBufOld(byteBuf);
    }
    
}
