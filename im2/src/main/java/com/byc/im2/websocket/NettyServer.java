package com.byc.im2.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by baiyc
 * 2020/5/20/020 14:41
 * Description：启动类
 */
@Slf4j
@Component
public class NettyServer extends Thread {

    @Value("${websocket.port}")
    private int port;

    public void run(){
        log.info("正在启动websocket服务器");
        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new WebsocketChannelInitializer());
            Channel channel = bootstrap.bind(port).sync().channel();
            log.info("webSocket服务器启动成功："+channel);
            log.info("webSocket服务器启动成功："+port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("运行出错："+e);
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("websocket服务器已关闭");
        }
    }

    @PostConstruct
    public void startServer() {
        this.start();
    }
}
