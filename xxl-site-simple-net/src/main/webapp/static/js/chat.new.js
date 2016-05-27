/**
 * 聊天室Demo
 * xuxueli 2016-5-26 17:35:06
 */
$(function() {
	
	var socket;
	if (!window.WebSocket) {
		window.WebSocket = window.MozWebSocket;
	}
	if (window.WebSocket) {
		socket = new WebSocket("ws://127.0.0.1:9998/chat");
		socket.onmessage = function(event) {
			var ta = document.getElementById('responseText');
			ta.value = ta.value + '\n' + event.data
		};
		socket.onopen = function(event) {
			var ta = document.getElementById('responseText');
			ta.value = "连接开启!";
		};
		socket.onclose = function(event) {
			var ta = document.getElementById('responseText');
			ta.value = ta.value + "连接被关闭";
		};
	} else {
		alert("你的浏览器不支持！");
	}

	function send(message) {
		if (!window.WebSocket) {
			return;
		}
		if (socket.readyState == WebSocket.OPEN) {
			socket.send(message);
		} else {
			alert("连接没有开启.");
		}
	}
	
	//WebChat.init();
});

/*
WebChat = function(me) {
	return me = {
		init : function() {
			if (!window.WebSocket) {
				alert("您的浏览器支持 WebSocket!");
				return;
			}
			
			var ws = new WebSocket("ws://localhost:9999/");
			ws.onopen = function() {
				ws.send("发送数据");
				alert("数据发送中...");
			};

			ws.onmessage = function(evt) {
				var received_msg = evt.data;
				alert("数据已接收...");
			};

			ws.onclose = function() {
				alert("连接已关闭...");
			};
		}
	};
}();
*/