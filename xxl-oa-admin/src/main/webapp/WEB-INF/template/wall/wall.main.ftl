<#import "/common/common.macro.ftl" as common>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>文章管理</title>
	<@common.common_style />
</head>
<body class="easyui-layout" fit="true">  
	<!-- toolbar -->
	<div id="tb" style="padding:5px;height:auto">
		<!-- search -->
		<div style="padding:2px;border:1px solid #ddd;">
			<form id="queryForm">
				内容:<input type="text" class="easyui-validatebox" data-options="validType:'length[0,20]'" style="width:200px" name="content">
			</form>
		</div>
		<!-- operate -->
		<div style="padding:2px;border:1px solid #ddd;">
			<a href="#" id="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'" >添加</a>
			<a href="#" id="cut" class="easyui-linkbutton" data-options="iconCls:'icon-cut'" >删除</a>
			<a href="#" id="edit" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" >编辑</a>
			&nbsp;|&nbsp;
			<a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			<a href="#" id="redo" class="easyui-linkbutton" iconCls="icon-redo">重置</a>
		</div>
	</div>
	<!-- DataGrid -->
	<table id="dg" title="一面墙" style="width:100%;height:100%;" ></table>

	<!-- add -->
	<div id="addWindow" class="easyui-window" title="新增" data-options="modal:true,closed:true,resizable:true" style="width:500px;height:400px; text-align:left;">
		<form id="addForm" method="post" >
			<br>
			<div>
				<label for="name">状态:</label>
				<select id="cc" class="easyui-combobox" data-options="required:true,editable:false" name="status" style="width:200px;">   
				    <option value="0">原始状态</option>
				    <option value="1">审核不通过</option>
				    <option value="2">审核通过</option>
				</select>
			</div>
			<br>
    		<div>
				<label for="name">内容:</label>
				<textarea class="easyui-validatebox" name="content" data-options="required:true, validType:['length[5,500]']" style="width:400px;height:200px;" ></textarea>
			</div>
			<br>
    		<div>
				<a class="easyui-linkbutton" icon="icon-ok" id="addOk">保存</a>
				<a class="easyui-linkbutton" icon="icon-cancel" id="addCancel">重置</a>
			</div>
		</form>
	</div>
	
	<!-- edit -->
	<div id="editWindow" class="easyui-window" title="更新" data-options="modal:true,closed:true,resizable:true" style="width:500px;height:400px; text-align:center;">
		<form id="editForm" method="post" >
		<input type="hidden" name="id"/>
		<input type="hidden" name="image"/>
    		<br>
			<div>
				<label for="name">状态:</label>
				<select id="cc" class="easyui-combobox" data-options="required:true,editable:false" name="status" style="width:200px;">   
				    <option value="0">原始状态</option>
				    <option value="1">审核不通过</option>
				    <option value="2">审核通过</option>
				</select>
			</div>
			<br>
    		<div>
				<label for="name">内容:</label>
				<textarea class="easyui-validatebox" name="content" data-options="required:true, validType:['length[5,500]']" style="width:400px;height:200px;" ></textarea>
			</div>
			<br>
    		<div>
				<a class="easyui-linkbutton" icon="icon-ok" id="editOk">保存</a>
				<a class="easyui-linkbutton" icon="icon-cancel" id="editCancel">重置</a>
			</div>
		</form>
	</div>
	
</body>
<script type="text/javascript" src="${request.contextPath}/static/js/wall/wall.main.1.js"></script>
</html>