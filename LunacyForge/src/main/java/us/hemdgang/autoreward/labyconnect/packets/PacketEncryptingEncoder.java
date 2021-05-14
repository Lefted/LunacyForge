package us.hemdgang.autoreward.labyconnect.packets;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncryptingEncoder extends MessageToByteEncoder<ByteBuf> {

    private final EncryptionTranslator encryptionCodec;

    public PacketEncryptingEncoder(final Cipher cipher) {
        this.encryptionCodec = new EncryptionTranslator(cipher);
    }

    protected void encode(final ChannelHandlerContext context, final ByteBuf byteBuf, final ByteBuf secondByteBuf) throws ShortBufferException, Exception {
	this.encryptionCodec.cipher(byteBuf, secondByteBuf);
    }
}
