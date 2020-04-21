package com.niton.net.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A utility class that encrypts or decrypts a file.
 *
 * @author www.codejava.net
 *
 */
public class SimpleAES {
	private static final String ALGORITHM = "AES";
	public static SecretKey lastKey;
	private static final int pad = 16;

	private final static byte[] iv = new byte[16];

	private static final String TRANSFORMATION = "AES/CBC/pkcs5padding";

	public static CipherOutputStream cryptOutputStream(SecretKey key, OutputStream os) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
		CipherOutputStream out = new CipherOutputStream(os, cipher);
		return out;
	}

	public static byte[] decrypt(SecretKey key, byte[] inputFile) {
		byte[] dec = doCrypto(Cipher.DECRYPT_MODE, key, inputFile);
		byte[] ret = new byte[dec.length - dec[dec.length - 1]];
		for (int i = 0; i < ret.length; i++)
			ret[i] = dec[i];
		return ret;
	}

	public static CipherInputStream decryptInputStream(SecretKey key, InputStream os) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
		CipherInputStream out = new CipherInputStream(os, cipher);
		return out;
	}

	public static Serializable decryptObject(SealedObject enc, SecretKey key) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			IOException, ClassNotFoundException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
		return (Serializable) enc.getObject(cipher);
	}

	private static byte[] doCrypto(int cipherMode, SecretKey key, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			cipher.init(cipherMode, key, ivspec);

			byte[] outputBytes = cipher.doFinal(data);

			return outputBytes;

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | InvalidAlgorithmParameterException ex) {
			ex.printStackTrace();
			return new byte[0];
		}
	}

	public static byte[] encrypt(SecretKey key, byte[] inputFile) {
		int padding = pad - inputFile.length % pad;
		byte[] padded = new byte[inputFile.length + padding];
		Random r = new Random();
		for (int i = 0; i < padded.length; i++)
			padded[i] = (byte) (inputFile.length > i ? inputFile[i] : r.nextInt());
		padded[padded.length - 1] = (byte) padding;
		return doCrypto(Cipher.ENCRYPT_MODE, key, padded);
	}

	public static SealedObject encryptObject(Serializable serial, SecretKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, IOException {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
		SealedObject so = new SealedObject(serial, cipher);
		return so;
	}

	public static SecretKey generateKey(int size) {
		KeyGenerator gen = null;
		try {
			gen = KeyGenerator.getInstance(ALGORITHM);
			gen.init(size);
			SecretKey key = gen.generateKey();
			lastKey = key;
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] generateRandom(int size) {
		Random r = new Random();
		byte[] b = new byte[size];
		for (int i = 0; i < b.length; i++)
			b[i] = (byte) r.nextInt(Byte.MAX_VALUE * 2);
		return b;
	}

	public static SecretKey getKey(byte[] bytes) {
		SecretKey key2 = new SecretKeySpec(bytes, 0, bytes.length, ALGORITHM);
		return key2;
	}

}
