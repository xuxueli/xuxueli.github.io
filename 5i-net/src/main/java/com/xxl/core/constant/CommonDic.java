package com.xxl.core.constant;


/**
 * 通用字典类
 * @author xuxueli
 */
public class CommonDic {
	
	/**
	 * Controller,通用视图
	 */
	public class CommonViewName {
		public static final String COMMON_RESULT = "common.result";			// 通用返回
		public static final String COMMON_EXCEPTION = "common.exception"; 	// 通用错误页
	}
	
	/**
	 * 文章菜单,字典
	 */
	public class ArticleMenuDic{
		public static final int MENU_MODULE_ID = 0;	// 菜单模块ID [二级菜单:菜单模块-菜单组]
	}
	
	/**
	 * HttpApplication上下文
	 */
	public class HttpApplicationDic {
	}

	/**
	 * HttpSession上下文
	 */
	public class HttpSessionKeyDic {
		public static final String LOGIN_IDENTITY = "login_identity";	// 用户登陆信息
	}
	
	/**
	 * 返回码
	 */
	public enum ReturnCodeEnum {
		SUCCESS("S"),
		FAIL("E");
		private String code;
		private ReturnCodeEnum(String code){
			this.code = code;
		}
		public String code(){
			return this.code;
		}
	}
	
}
