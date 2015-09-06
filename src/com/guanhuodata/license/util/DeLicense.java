package com.guanhuodata.license.util;

//Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://kpdus.tripod.com/jad.html
//Decompiler options: packimports(3) fieldsfirst ansi space 
//Source File Name:   ThreeDES.java

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DeLicense
{
	private static final String Algorithm = "DESede";
	public static final byte keyBytes[] = {
		17, 34, 79, 88, -120, 16, 64, 56, 40, 37, 
		121, 81, -53, -35, 85, 102, 119, 41, 116, -104, 
		48, 64, 54, -30
	};
	public static byte[] encryptMode(byte[] keybyte, byte[] src)
	{
		try
		{
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);

			return c1.doFinal(src);
		}
		catch (java.security.NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		}
		catch (javax.crypto.NoSuchPaddingException e2)
		{
			e2.printStackTrace();
		}
		catch (java.lang.Exception e3)
		{
			e3.printStackTrace();
		}

		return null;
	}
	
	public static byte[] decryptMode(byte[] keybyte, byte[] src)
	{
		try
		{
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		}
		catch (java.security.NoSuchAlgorithmException e1)
		{
			e1.printStackTrace();
		}
		catch (javax.crypto.NoSuchPaddingException e2)
		{
			e2.printStackTrace();
		}
		catch (java.lang.Exception e3)
		{
			e3.printStackTrace();
		}
		return null;
	}

	
	public static void main(String args[])
	{
		String srcFile = "E:\\works\\CTOMS\\ctoms\\ctoms-license\\src\\main\\resources\\license.lic";
		String desFile = "E:\\works\\CTOMS\\ctoms\\ctoms-license\\src\\main\\resources\\temp\\license.xml";
		String type = "1";//type=1读取aaa.lic文件，为1是生产license
		File file = new File(srcFile);
		int a = Integer.parseInt(Long.toString(file.length()));
		byte src[] = new byte[a];
		try
		{
			FileInputStream fis = new FileInputStream(file);
			fis.read(src);
			if ("0".equals(type))
			{//加密
				byte encodeBytes[] = encryptMode(keyBytes, src);
				FileOutputStream fos = new FileOutputStream(desFile);
				fos.write(encodeBytes);
				fos.flush();
				fos.close();
			} else
			{//解密
				byte encodeBytes[] = decryptMode(keyBytes, src);
				FileOutputStream fos = new FileOutputStream(desFile);
				fos.write(encodeBytes);
				fos.flush();
				fos.close();
			}
			System.out.println("Success");
		}
		catch (Exception exp)
		{
			System.out.println("可能文件目录不正确");
		}
	}

}