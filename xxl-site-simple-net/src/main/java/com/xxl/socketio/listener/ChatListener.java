package com.xxl.socketio.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.xxl.socketio.data.ChatObject;

/**
 * 消息处理
 * @author xuxueli
 */
public class ChatListener implements ConnectListener, DisconnectListener{
	private static Logger logger = LoggerFactory.getLogger(ChatListener.class);
	
	private SocketIOServer server;

	public ChatListener(SocketIOServer server) {
		this.server = server;
	}
	
	@OnConnect
	public void onConnect(SocketIOClient client) {
		// TODO Auto-generated method stub
		logger.info("新用户连接：{}", client.getRemoteAddress());
	}
	
	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		// TODO Auto-generated method stub
	}
	
	@OnEvent("chatevent")
	public void onEvent(SocketIOClient client, ChatObject data, AckRequest ackSender) {
		logger.info("中转消息：{}", data.toString());
		server.getBroadcastOperations().sendEvent("chatevent", data);
	}
	
}
