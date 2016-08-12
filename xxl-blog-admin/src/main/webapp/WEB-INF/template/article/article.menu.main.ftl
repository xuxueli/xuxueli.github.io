<#import "/common/common.macro.ftl" as common>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>文章菜单管理</title>
	<@common.common_style />
</head>
<body class="easyui-layout" fit="true">  
	<!-- toolbar -->
	<div id="tb" style="padding:5px;height:auto">
		<!-- operate -->
		<div style="padding:2px;border:1px solid #ddd;">
			<a href="#" id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a>
			<a href="#" id="cut" class="easyui-linkbutton" data-options="iconCls:'icon-cut'" >删除</a>
			<a href="#" id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >编辑</a>
		</div>
	</div>
	
	<!-- treegrid -->
	<table id="tg" class="easyui-treegrid"></table>
	
	<!-- add -->
	<div id="addWindow" class="easyui-window" title="添加菜单" data-options="modal:true,closed:true,iconCls:'icon-save', resizable:false" style="width:300px;height:300px; text-align:center;">
		<form id="addForm" method="post" >
    	<table cellpadding="5">
    		<tr>
    			<td>父节点ID:</td>
    			<td><input class="easyui-numberbox parentId" type="text" name="parentId" data-options="required:true, min:0,precision:0" value="0" /></td>
    		</tr>
    		<tr>
    			<td>顺序:</td>
    			<td><input class="easyui-numberbox" type="text" name="order" data-options="required:true, min:1,precision:0" value="1" /></td>
    		</tr>
    		<tr>
    			<td>名称:</td>
    			<td><input class="easyui-validatebox" type="text" name="name" data-options="required:true, validType:['length[2,10]']" /></td>
    		</tr>
    		<tr>
    			<td>描述:</td>
    			<td><textarea name="desc" rows="3" cols="20" ></textarea></td>
    		</tr>
    		<tr>
    			<td>点击量:</td>
    			<td><input class="easyui-numberbox" type="text" name="clickCount" data-options="required:true, min:0,precision:0" value="0" /></td>
    		</tr>
    		<tr>
    			<td colspan="2" >
    				<a class="easyui-linkbutton" icon="icon-ok" id="addOk">保存</a>
    				<a class="easyui-linkbutton" icon="icon-cancel" id="addCancel">重置</a>
				</td>
    		</tr>
    	</table>
		</form>
	</div>
	
	<!-- edit -->
	<div id="editWindow" class="easyui-window" title="编辑菜单" data-options="modal:true,closed:true,iconCls:'icon-save', resizable:false" style="width:300px;height:300px; text-align:center;">
		<form id="editForm" method="post" >
		<input type="hidden" name="menuId" />
    	<table cellpadding="5">
    		<tr>
    			<td>父节点ID:</td>
    			<td><input class="easyui-numberbox parentId" type="text" name="parentId" data-options="required:true, min:0,precision:0,readonly:true" value="0" /></td>
    		</tr>
    		<tr>
    			<td>顺序:</td>
    			<td><input class="easyui-numberbox" type="text" name="order" data-options="required:true, min:1,precision:0" value="1" /></td>
    		</tr>
    		<tr>
    			<td>名称:</td>
    			<td><input class="easyui-validatebox" type="text" name="name" data-options="required:true, validType:['length[2,10]']" /></td>
    		</tr>
    		<tr>
    			<td>描述:</td>
    			<td><textarea name="desc" rows="3" cols="20" ></textarea></td>
    		</tr>
    		<tr>
    			<td>点击量:</td>
    			<td><input class="easyui-numberbox" type="text" name="clickCount" data-options="required:true, min:0,precision:0" value="0" /></td>
    		</tr>
    		<tr>
    			<td colspan="2" >
    				<a class="easyui-linkbutton" icon="icon-ok" id="editOk">保存</a>
    				<a class="easyui-linkbutton" icon="icon-cancel" id="editCancel">重置</a>
				</td>
    		</tr>
    	</table>
		</form>
	</div>
	
</body>

<@common.hostUrl />
<script type="text/javascript" src="${request.contextPath}/static/js/article/article.menu.main.1.js"></script>
</html>