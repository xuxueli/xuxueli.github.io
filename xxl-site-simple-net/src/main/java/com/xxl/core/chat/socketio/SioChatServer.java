package com.xxl.core.chat.socketio;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.xxl.core.chat.socketio.listener.SioChatListener;

/**
 * socketio server
 * @author xuxueli 
 */
public class SioChatServer {
	
	public void init(){
		Configuration config = new Configuration();
		config.setHostname("127.0.0.1");
		config.setPort(9999);

		SocketIOServer server = new SocketIOServer(config);
		server.addListeners(new SioChatListener(server));

		server.start();
	}

}
