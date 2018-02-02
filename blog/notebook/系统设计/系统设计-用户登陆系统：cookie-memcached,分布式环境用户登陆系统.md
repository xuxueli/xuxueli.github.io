### Session和Cookie的定义以及区别

- 1、session保存在服务器，客户端不知道其中的信息；cookie保存在客户端，服务器能够知道其中的信息。
- 2、session中保存的是对象，cookie中保存的是字符串。
- 3、session不能区分路径，同一个用户在访问一个网站期间，所有的session在任何一个地方都可以访问到。而cookie中如果设置 了路径参数，那么同一个网站中不同路径下的cookie互相是访问不到的。
- 4、session默认需要借助cookie才能正常工作。如果客户端完全禁止cookie，session，这种方法将失效。但是如果服务器端启用了url编码，也就是用URLEncoder.encode("index.jsp?id=3","UTF-8");..把所有的url编码了，则会在url后面出现如下类似的东西index.jsp:jsessionid=fdsaffjdlksfd124324lkdjsf?id=3服务器通过这个进行session的判断。
- 5 session在用户会话结束后就会关闭了，但cookie因为保存在客户端，可以长期保存
- 6 cookie:是服务端向客户端写入的小的片段信息。cookie信息保存在服务器缓存区，不会在客户端显现。当你第一次登陆一个网站，服务器向你的机器写得片段信息。你可以在Internet选项中找到存放cookie的文件夹。如果不删除，cookie就一直在这个文件夹中。


### cookie + memcached分布式缓存，实现分布式环境下用户登陆验证

##### 1、登陆逻辑：
- 数据库验证：用户 “账号+密码” 的方式进行数据库登陆验证，校验通过进入下一步；
- 服务端缓存登陆信息：memcached缓存用户信息，格式：prefix_{userid} = 序列化用户登陆信息(刷新“随机usertoken”)
- 浏览器缓存登陆标记：cookie缓存用户登陆标记，格式：prefix = userid_随机usertoken

##### 2、注销逻辑：
- 服务端清除登陆缓存：remove prefix_{userid}
- 浏览器清除登陆缓存：remove prefix

##### 3、校验登陆状态：
- 解析浏览器cookie，获取userid和 “随机usertoken” ；
- 根据userid获取memcached中的登陆用户信息，校验 “随机usertoken”

##### 登陆Util
```
package com.xxl.util.core.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分布式登陆验证器
 * @author xuxueli 2015-12-12 18:09:04
 */
public class LoginUtil {
	
	private static final String LOGIN_IDENTITY_KEY = "LOGIN_IDENTITY";
	
	/**
	 * 服务端，用户登陆信息，分布式缓存key
	 * @param userid
	 * @return
	 */
	private static String cacheKey (int userid){
		return LOGIN_IDENTITY_KEY.concat("_").concat(String.valueOf(userid));
	}
	
	/**
	 * 客户端，用户登陆标记，cookie缓存value，格式化
	 * @param loginIdentity
	 * @return
	 */
	private static String cookieValue(LoginIdentity loginIdentity){
		return String.valueOf(loginIdentity.getUserid()).concat("_").concat(loginIdentity.getUsertoken());
	}
	
	/**
	 * 客户端，用户登陆标记，cookie缓存value，解析
	 * @param cookieValue
	 * @return
	 */
	private static LoginIdentity cookieValueParse(String cookieValue){
		String[] temp = cookieValue.split("_");
		LoginIdentity loginIdentity = new LoginIdentity();
		loginIdentity.setUserid(Integer.valueOf(temp[0]));
		loginIdentity.setUsertoken(temp[1]);
		return loginIdentity;
	}
	
	/**
	 * 登陆成功触发的逻辑
	 * @param response
	 * @param loginIdentity
	 * @return
	 */
	public static boolean login(HttpServletResponse response, LoginIdentity loginIdentity){
		XMemcachedUtil.set(cacheKey(loginIdentity.getUserid()), loginIdentity);
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, cookieValue(loginIdentity), true);
		return true;
	}
	
	/**
	 * 注销登陆触发的逻辑
	 * @param request
	 * @param response
	 * @param userid
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response, int userid){
		XMemcachedUtil.delete(cacheKey(userid));
		CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
	}
	
	/**
	 * 登陆状态监测
	 * @param request
	 * @return
	 */
	public static boolean loginCheck(HttpServletRequest request){
		String cookieValue = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		LoginIdentity cookieLoginIdentity = cookieValueParse(cookieValue);
		LoginIdentity cacheLoginIdentity = (LoginIdentity) XMemcachedUtil.get(cacheKey(cookieLoginIdentity.getUserid()));
		if (cookieLoginIdentity!=null && cacheLoginIdentity!=null 
				&& cookieLoginIdentity.getUsertoken().equals(cacheLoginIdentity.getUsertoken())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 用户缓存登陆信息
	 * @author xuxueli 2016-7-5 19:59:23
	 */
	public static class LoginIdentity implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int userid;
		private String username;
		private String password;
		private String usertoken;
		
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUsertoken() {
			return usertoken;
		}
		public void setUsertoken(String usertoken) {
			this.usertoken = usertoken;
		}
	}
	
}

```

