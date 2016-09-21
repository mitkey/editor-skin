package com.libgdx.skin.editor.utils.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @作者 Mitkey
 * @时间 2016年9月7日 上午10:00:31
 * @类说明:字符串工具类
 * @版本 xx
 */
public class StrUtil {

	private static final String EMPTY = "";

	/**
	 * 字符串是否为空白 空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为不可见字符（如空格）<br>
	 * 3、不为""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotBlank(String str) {
		return false == isBlank(str);
	}

	/**
	 * 字符串是否为空，空的定义如下 1、为null <br>
	 * 2、为""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为""<br>
	 * 
	 * @param str
	 *            被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 当给定字符串为null时，转换为 Empty ---> [""]
	 * 
	 * @param str
	 *            被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String nullToEmpty(String str) {
		return str == null ? EMPTY : str;
	}

	/**
	 * 当给定字符串为空字符串时，转换为 null
	 * 
	 * @param str
	 *            被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String emptyToNull(String str) {
		return isEmpty(str) ? null : str;
	}

	/**
	 * 是否包含空字符串
	 * 
	 * @param strs
	 *            字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasEmpty(String... strs) {
		for (String str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去除字符串两边的空格符，如果为null返回null
	 * 
	 * @param str
	 *            字符串
	 * @return 处理后的字符串
	 */
	public static String trim(String str) {
		return (null == str) ? null : str.trim();
	}

	/** 指定随机多少位数字的随机值 */
	public static String getRandomDigit(int count) {
		StringBuilder builder = new StringBuilder();
		if (count <= 0) {
			throw new IllegalArgumentException("count can not <= 0");
		}
		for (int i = 0; i < count; i++) {
			builder.append((int) (Math.random() * 10));
		}
		return builder.toString();
	}

	/** 获取唯一的 id：去除 - 符好。3522e7aa-b277-4a19-8eab-8670ef103ae9 转变为 3522e7aab2774a198eab8670ef103ae9 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "");
	}

	/** 去除重复字符 */
	public static String cleanRepeatStr(String content) {
		if (isEmpty(content)) {
			return content;
		}

		// 仅添加不存在的字符到 list
		List<String> list = new ArrayList<String>();
		char[] charArray = content.toCharArray();
		for (char c : charArray) {
			if (list.indexOf(String.valueOf(c)) == -1) {
				list.add(String.valueOf(c));
			}
		}

		// 转化为 String
		StringBuffer buffer = new StringBuffer();
		for (String temp : list) {
			buffer.append(temp);
		}
		return buffer.toString();
	}

	/**
	 * 获得set或get方法对应的标准属性名<br/>
	 * 例如：setName 返回 name
	 * 
	 * @param getOrSetMethodName
	 * @return 如果是set或get方法名，返回field， 否则null
	 */
	public static String getGeneralField(String getOrSetMethodName) {
		if (getOrSetMethodName.startsWith("get") || getOrSetMethodName.startsWith("set")) {
			return cutPreAndLowerFirst(getOrSetMethodName, 3);
		}
		return null;
	}

	/**
	 * 生成set方法名<br/>
	 * 例如：name 返回 setName
	 * 
	 * @param fieldName
	 *            属性名
	 * @return setXxx
	 */
	public static String genSetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "set");
	}

	/**
	 * 生成get方法名<br/>
	 * 例如：name 返回 getName
	 * 
	 * @param fieldName
	 *            属性名
	 * @return getXxx
	 */
	public static String genGetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "get");
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br/>
	 * 例如：StrUtil.cutPreAndLowerFirst("setName",3); --> return name;
	 * 
	 * @param str
	 *            被处理的字符串
	 * @param preLength
	 *            去掉的长度
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String cutPreAndLowerFirst(String str, int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.substring(preLength + 1);
			}
			return String.valueOf(first);
		}
		return null;
	}

	/**
	 * 原字符串首字母大写并在其首部添加指定字符串 <br>
	 * 例如： StrUtil.upperFirstAndAddPre("name","get"); --> return getName
	 * 
	 * @param str
	 *            被处理的字符串
	 * @param preString
	 *            添加的首部
	 * @return 处理后的字符串
	 */
	public static String upperFirstAndAddPre(String str, String preString) {
		if (str == null || preString == null) {
			return null;
		}
		return preString + upperFirst(str);
	}

	/**
	 * 大写首字母<br>
	 * 例如： StrUtil.upperFirst("name"); --> return Name;
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 */
	public static String upperFirst(String str) {
		if (isBlank(str)) {
			return "";
		} else {
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		}
	}

	/**
	 * 小写首字母<br>
	 * 例如： StrUtil.upperFirst("Name"); --> return name;
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 */
	public static String lowerFirst(String str) {
		if (isBlank(str)) {
			return "";
		} else {
			return Character.toLowerCase(str.charAt(0)) + str.substring(1);
		}
	}

	/**
	 * 去掉指定前缀
	 * 
	 * @param str
	 *            字符串
	 * @param prefix
	 *            前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(String str, String prefix) {
		if (str != null && str.startsWith(prefix)) {
			return str.substring(prefix.length());
		}
		return str;
	}

	/**
	 * 忽略大小写去掉指定前缀
	 * 
	 * @param str
	 *            字符串
	 * @param prefix
	 *            前缀
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefixIgnoreCase(String str, String prefix) {
		if (str != null && str.toLowerCase().startsWith(prefix.toLowerCase())) {
			return str.substring(prefix.length());
		}
		return str;
	}

	/**
	 * 去掉指定后缀
	 * 
	 * @param str
	 *            字符串
	 * @param suffix
	 *            后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(String str, String suffix) {
		if (str != null && str.endsWith(suffix)) {
			return str.substring(0, str.length() - suffix.length());
		}
		return str;
	}

	/**
	 * 忽略大小写去掉指定后缀
	 * 
	 * @param str
	 *            字符串
	 * @param suffix
	 *            后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffixIgnoreCase(String str, String suffix) {
		if (str != null && str.toLowerCase().endsWith(suffix.toLowerCase())) {
			return str.substring(0, str.length() - suffix.length());
		}
		return str;
	}

	/**
	 * 清理空白字符
	 * 
	 * @param str
	 *            被清理的字符串
	 * @return 清理后的字符串
	 */
	public static String cleanBlank(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("\\s*", EMPTY);
	}

	/**
	 * 切分字符串<br>
	 * a#b#c -> [a,b,c] <br>
	 * a##b#c -> [a,"",b,c]
	 * 
	 * @param str
	 *            被切分的字符串
	 * @param separator
	 *            分隔符字符
	 * @return 切分后的集合
	 */
	public static List<String> split(String str, char separator) {
		return split(str, separator, 0);
	}

	/**
	 * 切分字符串
	 * 
	 * @param str
	 *            被切分的字符串
	 * @param separator
	 *            分隔符字符
	 * @param limit
	 *            限制分片数
	 * @return 切分后的集合
	 */
	public static List<String> split(String str, char separator, int limit) {
		if (str == null) {
			return null;
		}
		List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
		if (limit == 1) {
			list.add(str);
			return list;
		}

		boolean isNotEnd = true; // 未结束切分的标志
		int strLen = str.length();
		StringBuilder sb = new StringBuilder(strLen);
		for (int i = 0; i < strLen; i++) {
			char c = str.charAt(i);
			if (isNotEnd && c == separator) {
				list.add(sb.toString());
				// 清空StringBuilder
				sb.delete(0, sb.length());

				// 当达到切分上限-1的量时，将所剩字符全部作为最后一个串
				if (limit != 0 && list.size() == limit - 1) {
					isNotEnd = false;
				}
			} else {
				sb.append(c);
			}
		}
		list.add(sb.toString());// 加入尾串
		return list;
	}

	/**
	 * 切分字符串<br>
	 * from jodd
	 * 
	 * @param str
	 *            被切分的字符串
	 * @param delimiter
	 *            分隔符
	 * @return 字符串
	 */
	public static String[] split(String str, String delimiter) {
		if (str == null) {
			return null;
		}
		if (str.trim().length() == 0) {
			return new String[]{str};
		}

		int dellen = delimiter.length(); // del length
		int maxparts = (str.length() / dellen) + 2; // one more for the last
		int[] positions = new int[maxparts];

		int i, j = 0;
		int count = 0;
		positions[0] = -dellen;
		while ((i = str.indexOf(delimiter, j)) != -1) {
			count++;
			positions[count] = i;
			j = i + dellen;
		}
		count++;
		positions[count] = str.length();

		String[] result = new String[count];

		for (i = 0; i < count; i++) {
			result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
		}
		return result;
	}

	/**
	 * 重复某个字符
	 * 
	 * @param c
	 *            被重复的字符
	 * @param count
	 *            重复的数目
	 * @return 重复字符字符串
	 */
	public static String repeat(char c, int count) {
		char[] result = new char[count];
		for (int i = 0; i < count; i++) {
			result[i] = c;
		}
		return new String(result);
	}

	/**
	 * 重复某个字符串
	 * 
	 * @param str
	 *            被重复的字符
	 * @param count
	 *            重复的数目
	 * @return 重复字符字符串
	 */
	public static String repeat(String str, int count) {

		// 检查
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		final char[] array = new char[size];
		str.getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * 比较两个字符串是否相同，如果为null或者空串则算不同
	 * 
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @return 是否非空相同
	 */
	public static boolean equalsNotEmpty(String str1, String str2) {
		if (isEmpty(str1)) {
			return false;
		}
		return str1.equals(str2);
	}

	/**
	 * 将多个对象字符化<br>
	 * 每个对象字符化后直接拼接，无分隔符
	 * 
	 * @param objs
	 *            对象数组
	 * @return 字符串
	 */
	public static String str(Object... objs) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * 将byte数组转为字符串
	 * 
	 * @param bytes
	 *            byte数组
	 * @param charset
	 *            字符集
	 * @return 字符串
	 */
	public static String str(byte[] bytes, String charset) {
		return new String(bytes, Charset.forName(charset));
	}

	/**
	 * 补充字符串以满足最小长度 <br>
	 * StrUtil.padPre("1", 3, '0'); --> return "001"
	 * 
	 * @param str
	 *            字符串
	 * @param minLength
	 *            最小长度
	 * @param padChar
	 *            补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(String str, int minLength, char padChar) {
		if (str.length() >= minLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = str.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 补充字符串以满足最小长度<br>
	 * StrUtil.padEnd("1", 3, '0'); --> return "100"
	 * 
	 * @param str
	 *            字符串
	 * @param minLength
	 *            最小长度
	 * @param padChar
	 *            补充的字符
	 * @return 补充后的字符串
	 */
	public static String padEnd(String str, int minLength, char padChar) {
		if (str.length() >= minLength) {
			return str;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(str);
		for (int i = str.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

}
