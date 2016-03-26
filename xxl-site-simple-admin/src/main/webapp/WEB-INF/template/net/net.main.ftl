<#import "/common/common.macro.ftl" as common>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>后台用户管理</title>
	<@common.common_style />
</head>
<body class="easyui-layout" fit="true">  

	<!-- toolbar -->
	<div id="tb" style="padding:5px;height:auto">
		<!-- operate -->
		<div style="padding:2px;border:1px solid #ddd;">
			一面墙数据&nbsp;|&nbsp;
			<a href="#" class="easyui-linkbutton wallClaw" runType="true" iconCls="icon-search">启动爬虫</a>
			<a href="#" class="easyui-linkbutton wallClaw" runType="false" iconCls="icon-redo">终止爬虫</a>
		</div>
	</div>

</body>
<@common.hostUrl />
<script type="text/javascript" src="${request.contextPath}/static/js/net/netMain.1.js"></script>
</html>