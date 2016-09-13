/**
 * 聊天室Demo
 * xuxueli 2016-5-26 17:35:06
 */
$(function() {
	WebChat.init();
	$("#send").click(function(){
		WebChat.send();
	});
});

WebChat = function(me) {
	return me = {
		userName:null,
		socket:null,
		init : function() {
			if (!window.WebSocket) {
				alert("你的浏览器不支持WebSocket！");
				return;
			}
			WebChat.userName = '用户' + Math.floor((Math.random() * 1000) + 1);
			WebChat.socket = new WebSocket("ws://127.0.0.1:9997/chat");
			WebChat.socket.onopen = function(event) {
				output('<span class="connect-msg">消息:</span>连接开启! ');
			};
			WebChat.socket.onclose = function(event) {
				output('<span class="connect-msg">消息:</span>连接被关闭! ');
			};
			WebChat.socket.onmessage = function(event) {
				if (WebChat.userName == WebChat.userName) {
					output('<span class="connect-msg">' + WebChat.userName + ':</span> ' + event.data);
				} else {
					output('<span class="username-msg">' + WebChat.userName + ':</span> ' + event.data);
				}
			};
			
			function output(message) {
				var currentTime = "<span class='time'>" + new Date().format('yyyy-MM-dd hh:mm:ss') + "</span>";
				var element = $("<div>" + currentTime + " " + message + "</div>");
				$('#console').prepend(element);
			}
		},
		send : function (message) {
			if (!window.WebSocket) {
				alert("你的浏览器不支持WebSocket！");
				return;
			}
			if (! (WebChat.socket.readyState == WebSocket.OPEN) ) {
				alert("连接没有开启！");
				return;
			}
			var message = $('#msg').val();
			if (!message) {
				return;
			}
			$('#msg').val('');
			WebChat.socket.send(message);
			
			/*WebChat.socket.send({
				userName : WebChat.userName,
				message : message
			});*/
			 
		}
	};
}();

//onkeydown
document.onkeydown = function keyDownSearch(e) {
	// 兼容FF和IE和Opera    
	var theEvent = e || window.event;
	var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
	if (code == 13) {
		WebChat.send();
	}
}

// data format
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
