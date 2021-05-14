package us.hemdgang.autoreward.labyconnect;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptManager {

    public static SecretKey createNewSharedKey() {
	try {
	    final KeyGenerator key = KeyGenerator.getInstance("AES");
	    key.init(128);
	    return key.generateKey();
	} catch (NoSuchAlgorithmException var1) {
	    throw new Error(var1);
	}
    }

    public static KeyPair createNewKeyPair() {
	try {
	    final KeyPairGenerator keyPair = KeyPairGenerator.getInstance("RSA");
	    keyPair.initialize(1024);
	    return keyPair.generateKeyPair();
	} catch (NoSuchAlgorithmException var1) {
	    var1.printStackTrace();
	    // Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Key pair generation failed!");
	    System.out.println("Key pair generation failed!");
	    return null;
	}
    }

    public static byte[] getServerIdHash(final String input, final PublicKey publicKey, final SecretKey secretKey) {
	try {
	    return digestOperation("SHA-1", new byte[][] { input.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded() });
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private static byte[] digestOperation(final String type, final byte[]... bytes) {
	try {
	    final MessageDigest disgest = MessageDigest.getInstance(type);
	    final byte[][] byts = bytes;
	    for (int length = bytes.length, i = 0; i < length; ++i) {
		final byte[] b = byts[i];
		disgest.update(b);
	    }
	    return disgest.digest();
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static PublicKey decodePublicKey(final byte[] p_75896_0_) {
	try {
	    final X509EncodedKeySpec var1 = new X509EncodedKeySpec(p_75896_0_);
	    final KeyFactory var2 = KeyFactory.getInstance("RSA");
	    return var2.generatePublic(var1);
	} catch (NoSuchAlgorithmException ex) {
	} catch (InvalidKeySpecException ex2) {
	}
	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Public key reconstitute failed!");
	System.out.println("Public key reconstitute failed!");
	return null;
    }

    public static SecretKey decryptSharedKey(final PrivateKey p_75887_0_, final byte[] p_75887_1_) {
	return new SecretKeySpec(decryptData(p_75887_0_, p_75887_1_), "AES");
    }

    public static byte[] encryptData(final Key p_75894_0_, final byte[] p_75894_1_) {
	return cipherOperation(1, p_75894_0_, p_75894_1_);
    }

    public static byte[] decryptData(final Key p_75889_0_, final byte[] p_75889_1_) {
	return cipherOperation(2, p_75889_0_, p_75889_1_);
    }

    private static byte[] cipherOperation(final int p_75885_0_, final Key p_75885_1_, final byte[] p_75885_2_) {
	try {
	    return createTheCipherInstance(p_75885_0_, p_75885_1_.getAlgorithm(), p_75885_1_).doFinal(p_75885_2_);
	} catch (IllegalBlockSizeException var4) {
	    var4.printStackTrace();
	} catch (BadPaddingException var5) {
	    var5.printStackTrace();
	}
	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Cipher data failed!");
	System.out.println("Cipher data failed!");
	return null;
    }

    private static Cipher createTheCipherInstance(final int p_75886_0_, final String p_75886_1_, final Key p_75886_2_) {
	try {
	    final Cipher var3 = Cipher.getInstance(p_75886_1_);
	    var3.init(p_75886_0_, p_75886_2_);
	    return var3;
	} catch (InvalidKeyException var4) {
	    var4.printStackTrace();
	} catch (NoSuchAlgorithmException var5) {
	    var5.printStackTrace();
	} catch (NoSuchPaddingException var6) {
	    var6.printStackTrace();
	}
	// Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Cipher creation failed!");
	System.out.println("Cipher creation failed!");
	return null;
    }

    public static Cipher createNetCipherInstance(final int opMode, final Key key) {
	try {
	    final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
	    cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
	    return cipher;
	} catch (GeneralSecurityException generalsecurityexception) {
	    throw new RuntimeException(generalsecurityexception);
	}
    }
}
