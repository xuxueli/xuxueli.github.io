<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	
	<#include "/net/common/common.style.ftl">
	<#import "/net/net.common.ftl" as netCommon>
	
</head>
<body>
<@netCommon.header />

<!-- content -->
<div class="container">
    
	<!--大标题-->
	<div class="jumbotron"><h4>邮箱找回密码.</h4></div>

	<form class="form-horizontal" role="form">
		<div class="form-group">
			<label for="firstname" class="col-sm-4 control-label">账号</label>
	      	<div class="col-sm-4">
				<input type="text" class="form-control" id="firstname" placeholder="请输入账号">
			</div>
	   	</div>
	   	<div class="form-group">
			<label for="firstname" class="col-sm-4 control-label">邮箱</label>
	      	<div class="col-sm-4">
				<input type="text" class="form-control" id="firstname" placeholder="请输入邮箱">
			</div>
	   	</div>
	   	<div class="form-group">
			<label for="firstname" class="col-sm-4 control-label">验证码</label>
	      	<div class="col-sm-4">
				<input type="text" class="form-control" id="firstname" placeholder="请输入验证码">
			</div>
	   	</div>
	   	<div class="form-group">
	      	<div class="col-sm-offset-4 col-sm-4">
				<button type="submit" class="btn btn-default">下一步</button>
	      	</div>
	   	</div>
	</form>
    
</div>

<@netCommon.footer />
</body>
</html>