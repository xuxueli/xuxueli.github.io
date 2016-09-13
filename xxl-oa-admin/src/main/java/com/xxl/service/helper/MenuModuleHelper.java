package com.xxl.service.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xxl.core.constant.CommonDic.AdminRoleMenuDic;
import com.xxl.core.model.main.AdminMenu;
import com.xxl.core.util.JacksonUtil;

/**
 * 用户权限菜单，操作相关
 * @author xuxueli
 */
public class MenuModuleHelper {
	
	/**
	 * 权限编码ID (服务器拦截使用)
	 * @param menus
	 * @return
	 */
	public static String initMenePermissionNums(List<AdminMenu> menus){
		Set<Integer> set = new HashSet<Integer>();
		for (AdminMenu item : menus) {
			if (item.getPermessionNum() > 0) {
				set.add(item.getPermessionNum());
			}
		}
		StringBuffer sb = new StringBuffer();
		for (Integer item : set) {
			sb.append(item);
			sb.append(",");
		}
		return sb.toString();
	}
	
	/**
	 * 权限菜单,JSON tree
	 * @param menus
	 * @return
	 */
	public static String initMenuModuleJson(List<AdminMenu> menus){
		List<AdminMenu> menuModule = new ArrayList<AdminMenu>();
		for (AdminMenu module : menus) {
			if (module.getParentId() == AdminRoleMenuDic.MENU_MODULE_ID) {
				initSubMenus(module, menus);
				menuModule.add(module);
			}
		}
		return JacksonUtil.writeValueAsString(menuModule);
	}
	/** 获取子菜单  */
	private static void initSubMenus(AdminMenu father, List<AdminMenu> menus) {
		for (AdminMenu sub : menus) {
			if (father.getMenuId() == sub.getParentId()) {
				father.getSubMenus().add(sub);
				initSubMenus(sub, menus );
			}
		}
	}

	/**
	 * 权限菜单,JSON tree (easyui tree格式修正)
	 * @param menus
	 * @return
	 */
	public static String initMenuModuleEasyJson(List<AdminMenu> menus){
		String str = initMenuModuleJson(menus);
		str = str.replace("menuId", "id");
		str = str.replace("name", "text");
		str = str.replace("subMenus", "children");
		return str;
		
	}

	public static void main(String[] args) {
		AdminMenu menu1 = new AdminMenu();
		menu1.setMenuId(1);
		menu1.setParentId(0);
		menu1.setName("menu1");
		menu1.setUrl("menu1");
		menu1.setPermessionNum(33);

		AdminMenu menu11 = new AdminMenu();
		menu11.setMenuId(11);
		menu11.setParentId(1);
		menu11.setName("menu11");
		menu11.setUrl("menu11");

		AdminMenu menu12 = new AdminMenu();
		menu12.setMenuId(12);
		menu12.setParentId(1);
		menu12.setName("menu12");
		menu12.setUrl("menu12");
		
		List<AdminMenu> list = new ArrayList<AdminMenu>();
		list.add(menu1);
		list.add(menu11);
		list.add(menu12);
		
		System.out.println(initMenePermissionNums(list));
		System.out.println(initMenuModuleJson(list));
	}
}
