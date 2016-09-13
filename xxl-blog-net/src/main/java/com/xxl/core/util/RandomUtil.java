package com.xxl.core.util;

import java.util.Random;

public class RandomUtil {
	
	private static final String numbers = "0123456789";
	private static final String lowerChars = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * 随机字符串(数字组成)
	 * @param length
	 */
	public static String randNumberStr(int length) {
		StringBuffer result = new StringBuffer();
		Random rd = new Random();
		for (int i = 0; i < length; i++) {
			result.append( numbers.charAt(Math.abs(rd.nextInt()) % numbers.length()) );
		}
		return result.toString();
	}
	
	/**
	 * 随机字符串(字母组成)
	 * @param length
	 */
	public static String randLowerChars(int length) {
		StringBuffer result = new StringBuffer();
		Random rd = new Random();
		for (int i = 0; i < length; i++) {
			result.append( lowerChars.charAt(Math.abs(rd.nextInt()) % lowerChars.length()) );
		}
		return result.toString();
	}

	public static void main(String[] args) {
		System.out.println(randLowerChars(4));
	}

}
