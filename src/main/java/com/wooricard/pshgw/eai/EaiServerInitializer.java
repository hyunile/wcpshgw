package com.wooricard.pshgw.eai;

import com.wooricard.pshgw.eai.service.EaiServerService;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class EaiServerInitializer extends ChannelInitializer<SocketChannel> {
//	private static final Logger logger = LoggerFactory.getLogger(EaiServerInitializer.class);

	private EaiServerService service;
	private int layoutSize;
	
	public EaiServerInitializer(int layoutSize) {
		this.layoutSize = layoutSize;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {		
		EaiServerHandler handler = new EaiServerHandler();
		handler.setService(service);
		
		ch.pipeline().addLast(
			new FixedLengthFrameDecoder(layoutSize),
			new ByteArrayDecoder(),
			new ByteArrayEncoder(),
			handler
		);
		
//		logger.info("initChannel : channel added."); 
	}

	public void setService(EaiServerService service) {
		this.service = service;
	}
}
