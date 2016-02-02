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
	<style>
	#console {height: 100%;overflow: auto;}
	.username-msg {color: orange;}
	.connect-msg {color: green;}
	.disconnect-msg {color: red;}
	.send-msg {color: #888}
	</style>
	
</head>
<body navKey="chat" >
<@netCommon.header />

<!-- content -->
<div class="container">

	<!--中央区域-->
    <div class="row">
    	<!--左侧-->
        <div class="col-md-9">

			<body navKey="chat" >
				<form class="well form-inline" onsubmit="return false;">
					<input id="msg" class="input-xlarge" type="text" placeholder="请输入..." />
					<button type="button" onClick="sendMessage()" class="btn">发送</button>
				</form>
				<div id="console" class="well"></div>
			</body>
			
        </div>
        <!--右侧-->
		<div class="col-md-3" >
			<@netCommon.right />
		</div>
    </div>
    
</div>

<@netCommon.footer />
</body>
<script>

	// 校验登陆状态
	if (!ifLogin) {
		ComAlert.show(2, "聊天室仅针对注册用户开放");
		return;
	}

	var userName = 'user' + Math.floor((Math.random() * 1000) + 1);
	var socket = io.connect('http://localhost:9999', {
		'reconnection delay' : 2000,
		'force new connection' : true
	});
	
	socket.on('connect', function() {
		output('<span class="connect-msg"> ' + userName + ' 客户端连接成功!</span>');
	});

	socket.on('disconnect', function() {
		output('<span class="disconnect-msg">客户端断开连接!</span>');
	});
	
	socket.on('chatevent', function(data) {
		output('<span class="username-msg">' + data.userName + ':</span> ' + data.message);
	});

	function sendDisconnect() {
		socket.disconnect();
	}

	// 向服务器发送数据，json格式
	function sendMessage() {
		var message = $('#msg').val();
		if (!message) {
			return;
		}
		$('#msg').val('');
		socket.emit('chatevent', {
			userName : userName,
			message : message
		});
	}

	function output(message) {
		var currentTime = "<span class='time'>" + new Date().format('yyyy-MM-dd hh:mm:ss') + "</span>";
		var element = $("<div>" + currentTime + " " + message + "</div>");
		$('#console').prepend(element);
	}
	
	document.onkeydown = function keyDownSearch(e) {
		// 兼容FF和IE和Opera    
		var theEvent = e || window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (code == 13) {
			sendMessage();
		}
	}

	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, //month
			"d+" : this.getDate(), //day
			"h+" : this.getHours(), //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
			"S" : this.getMilliseconds()
		//millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}
</script>

</html>