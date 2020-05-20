package com.byc.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by baiyc
 * 2020/5/20/020 17:05
 * Description：
 */
public class Client {
    public static void main(String[] args) throws Exception{
        EventLoopGroup group=new NioEventLoopGroup();
        Bootstrap boot=new Bootstrap();
        boot.option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .group(group)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new ChannelHandler[]{new HttpClientCodec(),
                                new HttpObjectAggregator(1024*1024*10)});
                        p.addLast("hookedHandler", new WebSocketClientHandler());
                    }
                });
        URI websocketURI = new URI("ws://localhost:8899/ws");
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        //进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, null, true,httpHeaders);
        System.out.println("connect");
        final Channel channel=boot.connect(websocketURI.getHost(),websocketURI.getPort()).sync().channel();
        WebSocketClientHandler handler = (WebSocketClientHandler)channel.pipeline().get("hookedHandler");
        handler.setHandshaker(handshaker);
        handshaker.handshake(channel);
        //阻塞等待是否握手成功
        handler.handshakeFuture().sync();

        Thread text=new Thread(() -> {
            int i=30;
            while (i>0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("text send");
                TextWebSocketFrame frame = new TextWebSocketFrame("我是文本");
                channel.writeAndFlush(frame).addListener((channelFuture) -> {
                    if(channelFuture.isSuccess()){
                        System.out.println("text send success");
                    }else{
                        System.out.println("text send failed  "+channelFuture.cause().getMessage());
                    }
                });
            }
        });

        Thread bina=new Thread(() -> {
            File file=new File("F:\\wenjian\\微信图片_20190428144459.png");
            FileInputStream fin;
            try {
                fin = new FileInputStream(file);
                byte[] data=new byte[1024];
                while (fin.read(data)>0){
                    ByteBuf bf= Unpooled.buffer().writeBytes(data);
                    BinaryWebSocketFrame binaryWebSocketFrame=new BinaryWebSocketFrame(bf);
                    channel.writeAndFlush(binaryWebSocketFrame).addListener((channelFuture) -> {
                        if(channelFuture.isSuccess()){
                            System.out.println("bina send success");
                        }else{
                            System.out.println("bina send failed  "+channelFuture.cause().toString());
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        text.start();
        bina.start();
    }

}
