<#import "/common/common.macro.ftl" as common>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>后台用户管理</title>
	<@common.common_style />
</head>
<body class="easyui-layout" fit="true">  
	
	<!-- wall claw -->
	<div id="tb" style="padding:20px;height:auto">
		<!-- operate -->
		<div style="padding:2px;border:1px solid #ddd;">
			一面墙爬虫&nbsp;|&nbsp;
			<a href="#" class="easyui-linkbutton wallClaw" runType="true" iconCls="icon-search">启动爬虫</a>
			<a href="#" class="easyui-linkbutton wallClaw" runType="false" iconCls="icon-redo">终止爬虫</a>
		</div>
	</div>
	
	<!-- net html -->
	<div id="tb" style="padding:20px;height:auto">
		<!-- operate -->
		<div style="padding:2px;border:1px solid #ddd;">
			官网静态化&nbsp;|&nbsp;
			<a href="#" class="easyui-linkbutton netHtml" iconCls="icon-search">静态化</a>
			<a href="#" class="easyui-linkbutton editNetAddress" iconCls="icon-redo">官网地址</a>
		</div>
	</div>
	
	<!-- edit net address -->
	<div id="editNetAddressWindow" class="easyui-window" title="官网地址更新" data-options="modal:true,closed:true,resizable:true" style="width:500px;height:250px; text-align:center;">
		<br>
		<div>
			<textarea class="easyui-validatebox" id="netAddress" data-options="required:true, validType:['length[5,250]']" style="width:454px;height:135px;" ></textarea>
		</div><br>
		<div>
			<a class="easyui-linkbutton" icon="icon-ok" id="editNetAddressOk">保存</a>
		</div><br>
	</div>

</body>
<@common.hostUrl />
<script type="text/javascript" src="${request.contextPath}/static/js/net/netMain.1.js"></script>
</html>