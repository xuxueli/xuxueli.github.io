package com.xxl.core.chat.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Text WebSocket Handler
 * @author xuxueli 2016-5-27 18:02:02
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	private static Logger logger = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);
	
	// client channel group
	private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		group.writeAndFlush(msg.retain());
		logger.info("消息内容: {}", msg.text());
		/*Channel incoming = ctx.channel();
        for (Channel channel : group) {
            if (channel != incoming){
                channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
            }
        }*/
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		logger.debug("channelReadComplete at: {}", ctx.channel().remoteAddress());
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client 在线, remoteAddress: {}", ctx.channel().remoteAddress());
    }
	
	@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// ctx.pipeline().remove(FullHttpRequestHandler.class);	// 握手成功
        //group.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + ctx.channel().remoteAddress() + " 加入"));
        group.add(ctx.channel());
        logger.info("Client 加入, remoteAddress: {}", ctx.channel().remoteAddress());
    }
	
	@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        /*for (Channel channel : group) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + ctx.channel().remoteAddress() + " 离开"));
        }*/
        //group.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + ctx.channel().remoteAddress() + " 离开"));
        group.remove(ctx.channel());
        logger.info("Client 离开, remoteAddress: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("Client 掉线, remoteAddress: {}", ctx.channel().remoteAddress());
    }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
		logger.info("Client 异常, remoteAddress: {}", ctx.channel().remoteAddress());
	}
	
}
