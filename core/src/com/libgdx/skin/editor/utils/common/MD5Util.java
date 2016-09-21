package com.libgdx.skin.editor.utils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @作者 Mitkey
 * @时间 2015年11月4日 下午3:21:53
 * @类说明: 安全工具,用于加密
 * @版本 xx
 */
public class MD5Util {
	/** 用于建立十六进制字符的输出的小写字符数组 */
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/** 用于建立十六进制字符的输出的大写字符数组 */
	private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	/** 用来将字节转换成 16 进制表示的字符 */
	private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 将字节数组转换为十六进制字符数组
	 * 
	 * @param data
	 *            byte[]
	 * @return 十六进制char[]
	 */
	public static char[] encodeHex(byte[] data) {
		return encodeHex(data, true);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 * 
	 * @param data
	 *            byte[]
	 * @param toLowerCase
	 *            <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制char[]
	 */
	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * 将字节数组转换为十六进制字符数组
	 * 
	 * @param data
	 *            byte[]
	 * @param toDigits
	 *            用于控制输出的char[]
	 * @return 十六进制char[]
	 */
	protected static char[] encodeHex(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param data
	 *            byte[]
	 * @return 十六进制String
	 */
	public static String encodeHexStr(byte[] data) {
		return encodeHexStr(data, true);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param data
	 *            byte[]
	 * @param toLowerCase
	 *            <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
	 * @return 十六进制String
	 */
	public static String encodeHexStr(byte[] data, boolean toLowerCase) {
		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param data
	 *            byte[]
	 * @param toDigits
	 *            用于控制输出的char[]
	 * @return 十六进制String
	 */
	protected static String encodeHexStr(byte[] data, char[] toDigits) {
		return new String(encodeHex(data, toDigits));
	}

	/**
	 * 将十六进制字符数组转换为字节数组
	 * 
	 * @param data
	 *            十六进制char[]
	 * @return byte[]
	 * @throws RuntimeException
	 *             如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
	 */
	public static byte[] decodeHex(char[] data) {

		int len = data.length;

		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}

	/**
	 * 将十六进制字符转换成一个整数
	 * 
	 * @param ch
	 *            十六进制char
	 * @param index
	 *            十六进制字符在字符数组中的位置
	 * @return 一个整数
	 * @throws RuntimeException
	 *             当ch不是一个合法的十六进制字符时，抛出运行时异常
	 */
	protected static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	public static String toHMACSHA256String(String myKey, String content) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret = new SecretKeySpec(myKey.getBytes(), "HmacSHA256");
			mac.init(secret);
			byte[] digest = mac.doFinal(content.getBytes());
			return encodeHexStr(digest);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
	}

	public static String getMD5(byte[] source) {
		// 返回实现指定摘要算法的MessageDigest对象。
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("没有Md5这个算法");
		}
		// 先将字符串转换成byte数组，再用byte 数组更新摘要
		md.update(source);
		// MD5 的计算结果是一个 128 位的长整数， 哈希计算，即加密
		byte tmp[] = md.digest();
		// 加密的结果是byte数组，将byte数组转换成字符串
		return bytes2Hex(tmp);
	}
	private static String bytes2Hex(byte[] bytes) {
		// 用字节表示就是 16 个字节。每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
		char str[] = new char[16 * 2];
		// 表示转换结果中对应的字符位置
		int k = 0;
		// 从第一个字节开始，对 MD5 的每一个字节
		for (int i = 0; i < 16; i++) {
			// 转换成 16 进制字符的转换
			byte byte0 = bytes[i]; // 取第 i 个字节
			// 取字节中高 4 位的数字转换,
			str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
			// 取字节中低 4 位的数字转换
			str[k++] = HEX_DIGITS[byte0 & 0xf];
		}
		return new String(str);
	}
}
