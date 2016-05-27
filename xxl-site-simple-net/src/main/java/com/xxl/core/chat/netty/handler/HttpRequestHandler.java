package com.xxl.core.chat.netty.handler;

import java.io.RandomAccessFile;

import com.xxl.core.chat.netty.NettyChatServer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		
		if(NettyChatServer.chatUrl.equalsIgnoreCase(msg.uri())){
			ctx.fireChannelRead(msg.retain());
			return;
		}
		
		if(HttpUtil.is100ContinueExpected(msg)){
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
			ctx.writeAndFlush(response);
		}
		
		RandomAccessFile file = new RandomAccessFile(HttpRequestHandler.class.getResource("/").getPath()+"/index.html", "r");
		HttpResponse response = new DefaultHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		
		boolean isKeepAlive = HttpUtil.isKeepAlive(msg);
		if(isKeepAlive){
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		
		ctx.write(response);
		if(ctx.pipeline().get(SslHandler.class) == null){
			ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
		}else{
			ctx.write(new ChunkedNioFile(file.getChannel()));
		}
		
		ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if(isKeepAlive == false){
			future.addListener(ChannelFutureListener.CLOSE);
		}
		
		file.close();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace(System.err);
	}
}
