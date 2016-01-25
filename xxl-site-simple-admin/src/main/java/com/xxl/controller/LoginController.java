package com.xxl.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.constant.CommonDic.HttpApplicationDic;
import com.xxl.core.constant.CommonDic.HttpSessionKeyDic;
import com.xxl.core.model.main.AdminRole;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.HttpSessionUtil;
import com.xxl.core.util.JacksonUtil;
import com.xxl.core.util.ServletContextUtil;
import com.xxl.dao.IAdminRoleDao;
import com.xxl.service.ILoginService;

/**
 * 登陆相关
 * @author xuxueli
 */
@Controller
public class LoginController {
	
	@Autowired
	private ILoginService loginService;
	@Autowired
	private IAdminRoleDao adminRoleDao;
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("")
	@PermessionType(loginState = false)
	public String index(HttpServletRequest request, HttpServletResponse response, 
			ModelMap resultMap, HttpSession session, RedirectAttributes redirect) {
		
		LoginIdentity identity = (LoginIdentity) HttpSessionUtil.get(session, HttpSessionKeyDic.LOGIN_IDENTITY);
		if (identity != null) {
			redirect.addAttribute("from", "sessionLogin");
			return "redirect:/home";
		}
		
		// 全局.角色列表
		ServletContext application = session.getServletContext();
		String loginRoleJson = (String) ServletContextUtil.get(application, HttpApplicationDic.LOGIN_ROLE_LIST);
		if (StringUtils.isBlank(loginRoleJson)) {
			List<AdminRole> allRole = adminRoleDao.getAllRole();
			loginRoleJson = JacksonUtil.writeValueAsString(allRole);
			ServletContextUtil.set(application, HttpApplicationDic.LOGIN_ROLE_LIST, loginRoleJson);
		}
		
		return "login";
	}
	
	/**
	 * 主界面
	 * @return
	 */
	@RequestMapping("/home")
	@PermessionType()
	public String home(HttpServletRequest request, HttpServletResponse response, ModelMap resultMap) {
		return "home";
	}
	
	/**
	 * 登陆
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	@PermessionType(loginState = false, randCode=true)
	public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, 
			ModelMap resultMap, HttpSession session, String username, String password,@RequestParam(defaultValue="-1") int role_id) {
		ReturnT<String> result = loginService.login(session, username, password, role_id);
		return result;
	}
	
	/**
	 * 注销登陆
	 * @return
	 */
	@RequestMapping("/logout")
	@ResponseBody
	@PermessionType()
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response, 
			ModelMap resultMap, HttpSession session) {
		
		ReturnT<String> result = loginService.logout(session);
		return result;
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping("/modifyPwd")
	@ResponseBody
	@PermessionType()
	public ReturnT<String> modifyPwd(HttpServletRequest request, HttpServletResponse response, 
			HttpSession session, String password, String newPwd, String reNewPwd) {
		
		ReturnT<String> result =loginService.modifyPwd(session, password, newPwd, reNewPwd);
		return result;
	}
	
}
