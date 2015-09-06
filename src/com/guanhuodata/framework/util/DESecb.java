package com.guanhuodata.framework.util;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
public class DESecb {
	static String DES = "DES/ECB/NoPadding";
	static String TriDes = "DESede/ECB/NoPadding";

	private static String prKey ="313233343536373838373635343332317177657274797569";
	public void setPrKey(String prKey) {
		this.prKey = prKey;
	}
	
	public static String encrypt(byte key[], byte data[]) {
		try {
			
			key=HexString2Bytes(prKey);
			
			byte[] k = new byte[24];
			int len = data.length;
			if (data.length % 8 != 0) {
				len = data.length - data.length % 8 + 8;
			}
			byte[] needData = null;
			if (len != 0)
				needData = new byte[len];
			for (int i = 0; i < len; i++) {
				needData[i] = 0x20;
			}
			System.arraycopy(data, 0, needData, 0, data.length);
			if (key.length == 16) {
				System.arraycopy(key, 0, k, 0, key.length);
				System.arraycopy(key, 0, k, 16, 8);
			} else {
				System.arraycopy(key, 0, k, 0, 24);
			}
			KeySpec ks = new DESedeKeySpec(k);
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
			SecretKey ky = kf.generateSecret(ks);
			Cipher c = Cipher.getInstance(TriDes);
			c.init(Cipher.ENCRYPT_MODE, ky);
			String en_result = byteArr2HexStr(c.doFinal(needData));
			return en_result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static String decrypt(byte key[], String deStr) {
		try {
			key=HexString2Bytes(prKey);
			byte data[] = HexString2Bytes(deStr);
			byte[] k = new byte[24];
			int len = data.length;
			if (data.length % 8 != 0) {
				len = data.length - data.length % 8 + 8;
			}
			byte[] needData = null;
			if (len != 0)
				needData = new byte[len];

			for (int i = 0; i < len; i++) {
				needData[i] = 0x00;
			}
			System.arraycopy(data, 0, needData, 0, data.length);
			if (key.length == 16) {
				System.arraycopy(key, 0, k, 0, key.length);
				System.arraycopy(key, 0, k, 16, 8);
			} else {
				System.arraycopy(key, 0, k, 0, 24);
			}
			KeySpec ks = new DESedeKeySpec(k);
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
			SecretKey ky = kf.generateSecret(ks);
			Cipher c = Cipher.getInstance(TriDes);
			c.init(Cipher.DECRYPT_MODE, ky);
			String de_result = new String(c.doFinal(needData));
			return de_result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}



	private static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
	
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
		
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}

			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}


	public static byte[] HexString2Bytes(String sString) {
		int len = sString.length();
		byte[] result = new byte[len / 2];
		byte[] temp = sString.getBytes();
		for (int i = 0; i < len / 2; ++i) {
			result[i] = uniteBytes(temp[i * 2], temp[i * 2 + 1]);
		}
		return result;
	}
	private static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte result = (byte) (_b0 | _b1);
		return result;
	}
}