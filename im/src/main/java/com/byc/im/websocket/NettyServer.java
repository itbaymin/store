package com.byc.im.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by baiyc
 * 2020/5/20/020 14:41
 * Description：启动类
 */
@Component
public class NettyServer extends Thread {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Value("${websocket.port}")
    private int port;

    public void run(){
        logger.info("正在启动websocket服务器");
        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new WebsocketChannelInitializer());
            Channel channel = bootstrap.bind(port).sync().channel();
            logger.info("webSocket服务器启动成功："+channel);
            logger.info("webSocket服务器启动成功："+port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("运行出错："+e);
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            logger.info("websocket服务器已关闭");
        }
    }

    @PostConstruct
    public void startServer() {
        this.start();
    }
}
