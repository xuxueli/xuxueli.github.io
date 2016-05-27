package com.xxl.core.chat.netty;

import java.net.InetSocketAddress;

import com.xxl.core.chat.netty.handler.HttpRequestHandler;
import com.xxl.core.chat.netty.handler.TextWebSocketFrameHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * netty chat server
 * @author xuxueli
 */
public class NettyChatServer {
	public static String chatUrl = "/chat";
	
	public void init(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				final EventLoopGroup workerGroup = new NioEventLoopGroup();
				final ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
				final Channel channel;
				
				ServerBootstrap boot = new ServerBootstrap();
				boot.group(workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							
							pipeline.addLast(new HttpServerCodec());
							pipeline.addLast(new ChunkedWriteHandler());
							pipeline.addLast(new HttpObjectAggregator(64*1024));
							pipeline.addLast(new HttpRequestHandler());
							pipeline.addLast(new WebSocketServerProtocolHandler(chatUrl));
							pipeline.addLast(new TextWebSocketFrameHandler(group));
						}
					});
				
				ChannelFuture channelFuture = boot.bind(new InetSocketAddress(9998)).syncUninterruptibly();
				channel = channelFuture.channel();
				
				System.out.println("netty chat server start................");
				Runtime.getRuntime().addShutdownHook(new Thread(){
					@Override
					public void run() {
						if(channel != null){
							channel.close();
						}
						group.close();
						workerGroup.shutdownGracefully();
					}
				});
				channelFuture.channel().closeFuture().syncUninterruptibly();
				
			}
			
		});
		
	}
	
}
