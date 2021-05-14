package us.hemdgang.autoreward.labyconnect.packets;

import java.security.PublicKey;

import javax.crypto.SecretKey;

import us.hemdgang.autoreward.labyconnect.CryptManager;
import us.hemdgang.autoreward.labyconnect.Packet;
import us.hemdgang.autoreward.labyconnect.PacketBuf;
import us.hemdgang.autoreward.labyconnect.PacketHandler;

public class PacketEncryptionResponse extends Packet {
    
    private byte[] sharedSecret;
    private byte[] verifyToken;

    public PacketEncryptionResponse(final SecretKey key, final PublicKey publicKey, final byte[] hash) {
	this.sharedSecret = CryptManager.encryptData(publicKey, key.getEncoded());
	this.verifyToken = CryptManager.encryptData(publicKey, hash);
    }

    public PacketEncryptionResponse() {
    }

    public byte[] getSharedSecret() {
	return this.sharedSecret;
    }

    public byte[] getVerifyToken() {
	return this.verifyToken;
    }

    @Override
    public void read(final PacketBuf buf) {
	this.sharedSecret = buf.readByteArray();
	this.verifyToken = buf.readByteArray();
    }

    @Override
    public void write(final PacketBuf buf) {
	buf.writeByteArray(this.sharedSecret);
	buf.writeByteArray(this.verifyToken);
    }

    @Override
    public void handle(final PacketHandler packetHandler) {
	packetHandler.handle(this);
    }
}
