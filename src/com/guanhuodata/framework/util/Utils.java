package com.guanhuodata.framework.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Utils {

	private static long sequenceID = 0L;

	public static String timeToDateString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time));
	}

	public static String timeToDateWithoutTimeString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(time));
	}

	public static boolean isBlank(String str) {
		if (str != null && str.trim().length() > 0) {
			return false;
		}
		return true;
	}

	public static String getTimeStampStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		return sdf.format(new Date());
	}

	public static byte[] forAuthenticatorClient(String clientId, String sharedKey, String timeStamp) {
		byte[] p = new byte[7];
		for (int i = 0; i < p.length; i++) {
			p[i] = 0;
		}
		String ps = new String(p);
		return MD5.getMD5Bytes(clientId + ps + sharedKey + timeStamp);
	}

	public static boolean isValidIPv4(String ipStr) {
		String[] ip = Pattern.compile(".", Pattern.LITERAL).split(ipStr);
		if (ip.length == 4) {
			if (ip[0].matches("[0-9]+") && ip[1].matches("[0-9]+") && ip[2].matches("[0-9]+")
					&& ip[3].matches("[0-9]+")) {
				if (Integer.parseInt(ip[0]) < 256 && Integer.parseInt(ip[1]) < 256 && Integer.parseInt(ip[2]) < 256
						&& Integer.parseInt(ip[3]) < 256) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isValidZipCode(String zipCode) {

		if (zipCode.length() != 6 || !zipCode.matches("[0-9]+")) {
			return false;
		}
		return true;
	}

	public static boolean isValidEmail(String email) {
		if (!email.matches("^([a-z0-9A-Z]+[-_|\\.]*)*[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
			return false;
		}
		return true;
	}

	public static boolean isValidPhoneNum(String phoneNum) {
		if (!phoneNum.matches("[0-9+-]+")) {
			return false;
		}
		return true;
	}

	public static boolean isValidCellPhoneNum(String cellPhone) {
		if (cellPhone.length() != 11 || !cellPhone.matches("[0-9]+")) {
			return false;
		}
		return true;
	}

	public static boolean inIpRange(String ip, String startIP, String endIP) {
		long ipLong = transformIP2Long(ip);
		long start = transformIP2Long(startIP);
		long end = transformIP2Long(endIP);
		if (ipLong >= start && ipLong <= end) {
			return true;
		}
		return false;
	}

	public static boolean compareIPv4(String startIP, String endIP) {
		long start = transformIP2Long(startIP);
		long end = transformIP2Long(endIP);
		if (start <= end) {
			return true;
		}
		return false;
	}

	public static long getSequenceID() {
		if (sequenceID >= 4294967296L) {
			sequenceID = 0L;
			return sequenceID++;
		} else
			return sequenceID++;
	}

	private static long transformIP2Long(String ip) {
		String[] ipArr = ip.split("\\.");
		StringBuffer sb = new StringBuffer();
		for (String ipSeg : ipArr) {
			sb.append(ipSeg);
		}
		return Long.parseLong(sb.toString());
	}

	public static boolean isNumeric(String value) {
		if (!value.matches("[0-9]+")) {
			return false;
		}
		return true;
	}

	public synchronized static final String getMD5Str(String str) {
		return getMD5Str(str, null);
	}

	public static String str0Padding(String s, int len) {
		if (s.length() < len) {
			while (s.length() < len) {
				s += '\0';
			}
		}
		return s;
	}

	public synchronized static final String getMD5Str(String str, String charSet) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			if (charSet == null) {
				messageDigest.update(str.getBytes());
			} else {
				messageDigest.update(str.getBytes(charSet));
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println("md5 error:" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("md5 error:" + e.getMessage());
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}
}
