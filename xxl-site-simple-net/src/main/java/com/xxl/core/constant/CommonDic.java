package com.xxl.core.constant;


/**
 * 通用字典类
 * @author xuxueli
 */
public class CommonDic {
	
	/** 邮件状态 */
	public enum EmailStatus {
		UN_SEND(0),
		SEND(1);
		private int value;
		private EmailStatus(int value){
			this.value = value;
		}
		public int value(){
			return this.value;
		}
	}
	
	/** 邮件类型 */
	public enum EmailType {
		EMAIL_ACTIVATE(100);
		
		private int type;
		private EmailType(int type){
			this.type = type;
		}
		
		public int type(){
			return this.type;
		}
	}
	
	/**
	 * 账号状态
	 */
	public enum UserState {
		/** 未激活 */
		INACTIVE(0),
		/** 正常 */
		NORMAL(100);
		private int state;
		private UserState(int state){
			this.state = state;
		}
		public int state(){
			return this.state;
		}
	}
	
	/**
	 * Controller,通用视图
	 */
	public class CommonViewName {
		public static final String COMMON_RESULT = "net/common/common.result.body";			// 通用返回
		public static final String COMMON_EXCEPTION = "net/common/common.result.exception"; 	// 通用错误页
	}
	
	/**
	 * 文章菜单接口:模块-组-文章
	 */
	public class ArticleMenuDic{
		public static final int MENU_MODULE_ID = 0;	// 模块类型
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
