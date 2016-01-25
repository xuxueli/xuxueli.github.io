package com.xxl.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验工具
 * 
 * @author xuxueli
 */
public class RegexUtil {

	/**
	 * 正则匹配
	 * @param regex	: 正则表达式
	 * @param str	: 待匹配字符串
	 * @return
	 */
	public static boolean matches(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static boolean find(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	
	// 移动号码
	public static final String cmccRegex_v5 = "^[1]{1}(([3]{1}[4-9]{1}[0-9]{1})|([5]{1}[0-2|7-9]{1}[0-9]{1})|([8]{1}[2|3|4|7|8]{1}[0-9]{1})|([4]{1}[7]{1}[0-9]{1})|(705)|(78[0-9]{1}))[0-9]{7}$";
	// 联通号码
	public static final String cuccRegex_v5 = "^[1]{1}(([3]{1}[0-2]{1}[0-9]{1})|(45[0-9]{1})|([5]{1}[5|6]{1}[0-9]{1})|([8]{1}[5|6]{1}[0-9]{1})|(709)|(76[0-9]{1}))[0-9]{7}$";
	// 电信号码
	public static final String cncRegex_v5 = "^[1]{1}(([3]{1}[3]{1}[0-9]{1})|([5]{1}[3]{1}[0-9]{1})|([8]{1}[0|1|9]{1}[0-9]{1})|(700)|(77[0-9]{1}))[0-9]{7}$";

	// 纯中文
	public static final String chinese = "^[\u4e00-\u9fa5]+$";
	// 包含中文
	public static final String chinesePart = "[\u4E00-\u9FA5]";
	// 纯字母规则
	public static final String abcStandard = "^[A-Za-z]+$";
	// 纯数字规则
	public static final String numbericStandard = "[0-9]*";
	// 字母和数字的规则
	public static final String numbericAndAbc = "^[A-Za-z0-9]+$";
	// 邮箱规则
	public static final String emailStandard = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

	/**
	 * 是否为手机号码
	 */
	public static boolean isMobile(String mobile) {
		return iscmcc(mobile) || iscucc(mobile) || iscnc(mobile);
	}

	/**
	 * 是否为移动手机号段
	 */
	public static boolean iscmcc(String mobile) {
		return matches(cmccRegex_v5, mobile);
	}

	/**
	 * 是否为联通号码段
	 */
	public static boolean iscucc(String mobile) {
		return matches(cuccRegex_v5, mobile);
	}

	/**
	 * 是否为电信号段
	 */
	public static boolean iscnc(String mobile) {
		return matches(cncRegex_v5, mobile);
	}
	
	/**
	 * 纯数字字符串
	 */
	public static boolean isNumber(String str) {
		return matches(numbericStandard, str);
	}
	
	/**
	 * 是否包含中文
	 * @return
	 */
	public static boolean ifContainChinese(String str){
		return find(chinesePart, str);
	}

	public static void main(String[] args) {
		System.out.println(ifContainChinese("asdsssasdasd"));
		System.out.println(ifContainChinese("asds动ssasdasd"));
		String tel = "17701093170";
		if (iscmcc(tel)) {
			System.out.println("移动");
		} else if (iscucc(tel)) {
			System.out.println("联通");
		} else if (iscnc(tel)) {
			System.out.println("电信");
		} else {
			System.out.println("未知");
		}
	}

}