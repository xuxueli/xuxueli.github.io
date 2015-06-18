<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	
	<#include "/net/common/common.style.ftl">
	<#import "/net/net.common.ftl" as netCommon>
	
	<script type="text/javascript" src="${base_url}js/common/requestParam.js"></script>
	<script type="text/javascript" src="${base_url}js/email.activite.1.js"></script>
	
</head>
<body>
<@netCommon.header />

<!-- content -->
<div class="container">
    
	<!--大标题-->
	<div class="jumbotron"><h4>邮箱激活.</h4></div>

	<form class="form-horizontal" role="form" id="emailActivateForm" >
	   	<div class="form-group">
			<label for="firstname" class="col-sm-4 control-label">邮箱</label>
	      	<div class="col-sm-4">
				<input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱">
			</div>
	   	</div>
	   	<div class="form-group">
			<label for="firstname" class="col-sm-4 control-label">激活码</label>
	      	<div class="col-sm-4">
				<input type="text" class="form-control" name="sendCode" id="sendCode" placeholder="请输入验证码">
			</div>
	   	</div>
	   	<div class="form-group">
	      	<div class="col-sm-offset-4 col-sm-4">
				<button type="button" class="btn btn-default" id="emailActivate" >激活</button>
	      	</div>
	   	</div>
	</form>
    
</div>

<@netCommon.footer />
</body>
</html>