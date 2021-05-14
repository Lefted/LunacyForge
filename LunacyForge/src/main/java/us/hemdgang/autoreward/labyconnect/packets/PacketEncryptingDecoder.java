package us.hemdgang.autoreward.labyconnect.packets;


import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class PacketEncryptingDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final EncryptionTranslator decryptionCodec;

    public PacketEncryptingDecoder(final Cipher cipher) {
	this.decryptionCodec = new EncryptionTranslator(cipher);
    }

    protected void decode(final ChannelHandlerContext context, final ByteBuf byteBuf, final List<Object> list) throws ShortBufferException, Exception {
	list.add(this.decryptionCodec.decipher(context, byteBuf));
    }
}
