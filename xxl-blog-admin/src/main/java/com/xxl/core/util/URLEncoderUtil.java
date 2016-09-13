package com.xxl.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码解码
 * @author xuxueli
 */
public final class URLEncoderUtil {
	
	/**
	 * URLDecode编码 (utf-8)
	 * @param param
	 */
	public static String encoderUTF8(String param){
		try {
			param = URLEncoder.encode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return param;
	}
	
	/**
	 * URLDecode解码 (utf-8)
	 * @param param
	 */
	public static String decoderUTF8(String param){
		try {
			param = URLDecoder.decode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return param;
	}
	
}
