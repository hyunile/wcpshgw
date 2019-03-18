package com.wooricard.pshgw.eai;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wooricard.pshgw.eai.service.EaiServerService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
public class EaiServer {
	private static final Logger logger = LoggerFactory.getLogger(EaiServer.class);

	private static final int LAYOUT_SIZE = 299;
	
	@Autowired
	private EaiServerService service;

	@Value("${eai.listen.port}")
	private int port;
	
	private ServerBootstrap bootstrap;
	private ChannelFuture future;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	private EaiServerInitializer initializer;
	
	@PostConstruct
	public void init() {
		boolean result = initialize();
		logger.info("autoStart : initialize(), result = " + result);
		
		if(result) {
			if(start()) {
//				running(); 
				logger.info("autoStart : started.");			
			}
			else {
				logger.error("autoStart : Failed to start EaiServer.");			
				stop();
			}
		}
		else {
			logger.info("autoStart : failed to start.");
		}
	}
	
	@PreDestroy
	public void cleanUp() {
		stop();
		logger.info("cleanUp : stopped.");
	}
	
	public boolean initialize() {
		bootstrap = new ServerBootstrap();
		
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		
		initializer = new EaiServerInitializer(LAYOUT_SIZE);
		initializer.setService(service);
		
		try {
			bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(initializer);
		}
		catch(Exception e) {
			logger.error("initialize : ", e);
			return false;
		}
		
		return true;
	}
	
	public boolean start() {
		try {
			future = bootstrap.bind(port).sync();
		}
		catch(InterruptedException e) {
			logger.error("start : ", e);
			future = null;
			return false;
		}
		
		return true;
	}
	
	public void running() {
		if(future == null)
			return;
		
		try {
			future.channel().closeFuture().sync();
		}
		catch(InterruptedException e) {
			logger.error("running : ", e);
		}
	}
	
	public void stop() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
}
