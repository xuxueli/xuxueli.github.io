package com.xxl.service.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.model.main.UserMain;
import com.xxl.core.util.CookieUtil;
import com.xxl.core.util.HttpSessionUtil;

/**
 * 用户登陆信息，操作相关
 * @author Administrator
 *
 */
public class LoginIdentityHelper {
	// 用户登陆信息,缓存key (cookie=sameKey:cacheIdentity; cache:sameKey+userId:loginIdentity)
	public static final String LOGIN_IDENTITY = "login_identity";	// 
	
	// ---------------LoginIdentity-----------------------------------------
	/** cache缓存key:sameKey+userId */
	private static String cacheKey(LoginIdentity identity) {
		return LOGIN_IDENTITY.concat(String.valueOf(identity.getUserId())); 
	}
	
	/** cache缓存value:loginIdentity */
	private static LoginIdentity cacheValue(UserMain userMain){
		LoginIdentity identity = new LoginIdentity();
		identity.setUserId(userMain.getUserId());
		identity.setEmail(userMain.getEmail());
		//identity.setPassword(userMain.getPassword());
		identity.setUserToken(userMain.getUserToken());
		identity.setRealName(userMain.getRealName());
		identity.setState(userMain.getState());
		identity.setRegTime(userMain.getRegTime());
		return identity;
	}
	
	// ---------------CookieIdentity-----------------------------------------
	/** cookie.encry */
	private static String encryCookieIdentity(LoginIdentity identity){
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(identity.getUserId()));
		sb.append("#");
		sb.append(identity.getPassword());
		return sb.toString();
	}
	
	/** cookie.decry */
	private static LoginIdentity decryCookieIdentity(String encryInfo){
		if (encryInfo == null || encryInfo.trim().length() == 0) {
			return null;
		}
		String[] encryInfoArr = encryInfo.split("#");
		if (encryInfoArr == null || encryInfoArr.length < 2) {
			return null;
		}
		LoginIdentity cookieIdentity = new LoginIdentity();
		cookieIdentity.setUserId(Integer.valueOf(encryInfoArr[0]));
		cookieIdentity.setPassword(encryInfoArr[1]);
		return cookieIdentity;
	}
	
	// ---------------Login/LogOut-----------------------------------------
	/** login */
	public static void login(HttpServletResponse response, HttpSession session, UserMain userMain) {
		// cache
		LoginIdentity loginIdentity = cacheValue(userMain);
		String cacheKey = cacheKey(loginIdentity);
		HttpSessionUtil.set(session, cacheKey, loginIdentity);
		// cookie
		String cookieValue = encryCookieIdentity(loginIdentity);
		CookieUtil.set(response, LOGIN_IDENTITY, cookieValue);
		
	}

	/** logout */
	public static void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		LoginIdentity loginIdentity = decryCookieIdentity(CookieUtil.getValue(request, LOGIN_IDENTITY));
		if (loginIdentity != null) {
			// cache
			String cacheKey = cacheKey(loginIdentity);
			HttpSessionUtil.remove(session, cacheKey);
			// cookie
			CookieUtil.remove(request, response, LOGIN_IDENTITY);
		}
	}
	
	/** cookie check */
	public static LoginIdentity cookieCheck(HttpServletRequest request, HttpSession session) {
		LoginIdentity cookieIdentity = decryCookieIdentity(CookieUtil.getValue(request, LOGIN_IDENTITY));
		if (cookieIdentity != null) {
			return cookieIdentity;
		}
		return null;
	}
	
	/** cache check */
	public static LoginIdentity cacheCheck(HttpServletRequest request, HttpSession session) {
		LoginIdentity cookieIdentity = decryCookieIdentity(CookieUtil.getValue(request, LOGIN_IDENTITY));
		if (cookieIdentity != null) {
			return (LoginIdentity) HttpSessionUtil.get(session, cacheKey(cookieIdentity));
		}
		return null;
	}
	
}
