<#import "/net/common/common.macro.ftl" as netCommon>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>我爱</title>
	<@netCommon.common_hosturl />
	<@netCommon.common_style />
	<script type="text/javascript" src="${host_url}/static/plugin/socketio/socket.io-1.3.5.js"></script>
	<script type="text/javascript" src="${host_url}/static/js/chat.index.1.js"></script>
	<style>
	#console {height: 100%;overflow: auto;}
	.username-msg {color: orange;}
	.connect-msg {color: green;}
	.disconnect-msg {color: red;}
	.send-msg {color: #888}
	</style>
	
</head>
<body navKey="chat" >
<#--
<@netCommon.header />
-->
<!-- content -->
<div class="container">

	<!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-9">

			<body navKey="chat" >
				<form class="well form-inline" onsubmit="return false;">
					<input id="msg" class="input-xlarge" type="text" placeholder="请输入..." />
					<button type="button" id="send" class="btn">发送</button>
				</form>
				<div id="console" class="well"></div>
			</body>
			
        </div>
        <!--右侧-->
		<div class="col-md-3" >
			<#--
			<@netCommon.right />
			-->
		</div>
    </div>
    
</div>

<#--
<@netCommon.footer />
-->
</body>

</html>