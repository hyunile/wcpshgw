package com.wooricard.pshgw.eai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wooricard.pshgw.eai.service.EaiServerService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EaiServerHandler extends SimpleChannelInboundHandler<byte[]> {
	private static final Logger logger = LoggerFactory.getLogger(EaiServerHandler.class);

	private EaiServerService service;
	
	public EaiServerHandler() {
		super(true);
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		StringBuilder code = new StringBuilder();
		int result = service.saveData(msg, code);
		byte[] ack = service.makeAckData(msg, result);
		
		ctx.writeAndFlush(ack);
		logger.info("messageReceived : ack sent. code = " + code.toString());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(String.format("exceptionCaught : error  : %s : %s", cause.getClass().getName(), cause.getMessage()));
		ctx.close();
	}

	public void setService(EaiServerService service) {
		this.service = service;
	}
}
