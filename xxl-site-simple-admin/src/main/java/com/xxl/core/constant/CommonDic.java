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
		public static final String COMMON_RESULT = "common/common.result";			// 通用返回
		public static final String COMMON_EXCEPTION = "common/common.exception"; 	// 通用错误页
	}
	
	/**
	 * 一面墙,字典
	 */
	public class WallDic{
		// 数据来源
		public static final int WALL_SOURCE_BACK = 0;	// 后台管理员,录入
		public static final int WALL_SOURCE_CRAWlER = 1;// 爬虫
		public static final int WALL_SOURCE_FRONT = 2;	// 前台用户
		
		// 数据状态
		public static final int WALL_STATUS_ORIGIN = 0;	// 原始状态
		public static final int WALL_STATUS_UNPASS = 1;	// 审核不通过
		public static final int WALL_STATUS_PASS = 2;	// 审核通过
	}
	
	/**
	 * 文章菜单,字典
	 */
	public class ArticleMenuDic{
		public static final int MENU_MODULE_ID = 0;	// 菜单模块ID [二级菜单:菜单模块-菜单组]
	}
	
	/**
	 * 权限-角色,字典
	 */
	public class AdminRoleMenuDic{
		public static final int SUPER_ROLE_ID = 1;	// 超级管理员, 角色ID
		public static final int MENU_MODULE_ID = 0;	// 菜单模块ID [三级菜单:菜单模块-菜单组-菜单项]
	}
	
	/**
	 * HttpApplication上下文
	 */
	public class HttpApplicationDic {
		public static final String LOGIN_ROLE_LIST = "login_role_list";	// 登录角色列表
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
	
	/**
	 * 系统配置
	 */
	public enum SystemParamEnum {
		NET_ADDRESS("官网地址");
		String title;
		private SystemParamEnum(String title){
			this.title = title;
		}
		public String getTitle() {
			return title;
		}
	}
	
}
