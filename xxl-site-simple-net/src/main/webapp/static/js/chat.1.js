$(function(){
	// 校验登陆状态
	if ($.cookie('login_identity')) {
		$.post(base_url + "user/loginCheck", function(data, status) {
			if (data.code == "S") {
				initChat();
			} else {
				ComAlert.show(2, "聊天室仅针对注册登录用户开放");
				return;
			}
		}, 'json');
	} else {
		ComAlert.show(2, "聊天室仅针对注册登录用户开放");
		return;
	}
	
	function initChat(){
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
	}
	
});