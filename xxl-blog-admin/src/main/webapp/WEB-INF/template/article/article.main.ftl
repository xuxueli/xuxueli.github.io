<#import "/common/common.macro.ftl" as common>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>文章管理</title>
	<@common.common_style />
	<@common.common_style_ueditor />
</head>
<body class="easyui-layout" fit="true">  
	<!-- toolbar -->
	<div id="tb" style="padding:5px;height:auto">
		<!-- search -->
		<div style="padding:2px;border:1px solid #ddd;">
			<form id="queryForm">
				标题:<input type="text" class="easyui-validatebox" data-options="validType:'length[0,20]'" style="width:100px" name="title">
				菜单:<input class="easyui-combotree" id="query-menuId" name="menuId" data-options="required:false" style="width:200px;" >
			</form>
		</div>
		<!-- operate -->
		<div style="padding:2px;border:1px solid #ddd;">
			<a href="#" id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a>
			<a href="#" id="cut" class="easyui-linkbutton" data-options="iconCls:'icon-cut'" >删除</a>
			<a href="#" id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >编辑</a>
			<a href="#" id="view" class="easyui-linkbutton" data-options="iconCls:'icon-'" >预览</a>
			&nbsp;|&nbsp;
			<a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			<a href="#" id="redo" class="easyui-linkbutton" iconCls="icon-redo">重置</a>
		</div>
	</div>
	<!-- DataGrid -->
	<table id="dg" title="文章列表" style="width:100%;height:100%;" ></table>

	<!-- add -->
	<div id="addWindow" class="easyui-window" title="新增文章" data-options="modal:true,closed:true,resizable:true,maximized:true" style="width:90%;height:90%; text-align:center;">
		<form id="addForm" method="post" >
    	<table >
    		<tr>
    			<td><script id="add-content" name="content" type="text/plain" style2="width:100%;height:100%;"></script></td>
    		</tr>
    		<tr style="float:left;width:100%">
    			<td>标题:<input class="easyui-validatebox" type="text" name="title" data-options="required:true, validType:['length[5,50]']" style="width:300px;" /></td>
    		</tr>
    		<tr style="float:left;width:100%">
    			<td>菜单:<input class="easyui-combotree" id="add-menuId" name="menuId" data-options="required:true" style="width:200px;" ></td>
    		</tr>
    		<tr style="float:left;width:100%">
    			<td>
    				<a class="easyui-linkbutton" icon="icon-ok" id="addOk">保存</a>
    				<a class="easyui-linkbutton" icon="icon-cancel" id="addCancel">重置</a>
				</td>
    		</tr>
    	</table>
		</form>
	</div>
	
	<!-- edit -->
	<div id="editWindow" class="easyui-window" title="新增文章" data-options="modal:true,closed:true,resizable:true,maximized:true" style="width:90%;height:90%; text-align:center;">
		<form id="editForm" method="post" >
		<input type="hidden" name="articleId"/>
    	<table >
    		<tr>
    			<td><script id="edit-content" name="content" type="text/plain" ></script></td>
    		</tr>
    		<tr style="float:left;width:100%">
    			<td>标题:<input class="easyui-validatebox" type="text" name="title" data-options="required:true, validType:['length[5,50]']" style="width:300px;" /></td>
    		</tr>
    		<tr style="float:left;width:100%">
    			<td>菜单:<input class="easyui-combotree" id="edit-menuId" name="menuId" data-options="required:true" style="width:200px;" /></td>
    		</tr>
    		<tr style="float:left;width:100%">
    			<td>
    				<a class="easyui-linkbutton" icon="icon-ok" id="editOk">保存</a>
    				<a class="easyui-linkbutton" icon="icon-cancel" id="editCancel">重置</a>
				</td>
    		</tr>
    	</table>
		</form>
	</div>
	
	<!-- view -->
	<div id="viewWindow" class="easyui-window" title="预览文章" data-options="modal:true,closed:true,resizable:true,maximized:true" style="width:90%;height:90%; text-align:center;"></div>
	
</body>

<script type="text/javascript" >
	// 文章菜单
	var articleMenuJson = eval( '${articleMenuJson?if_exists}' );
</script>
<@common.hostUrl />
<script type="text/javascript" src="${request.contextPath}/static/js/article/article.main.1.js"></script>
</html>