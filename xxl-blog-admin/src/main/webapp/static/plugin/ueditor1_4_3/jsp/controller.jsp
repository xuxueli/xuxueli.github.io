<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
	request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	
	//out.write( new ActionEnter( request, rootPath ).exec() );
	
	// 在线管理文件,获取绝对路径bug解决 start
	String result = new ActionEnter(request, rootPath).exec();
	String action = request.getParameter("action");
	if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
		rootPath = rootPath.replace("\\", "/");
		result = result.replaceAll(rootPath, "/");//把返回路径中的物理路径替换为 '/'
	}
	out.write(result);
	// 在线管理文件,获取绝对路径bug解决 stop
	
%>