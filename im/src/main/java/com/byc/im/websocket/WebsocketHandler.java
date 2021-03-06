package com.byc.im.websocket;

import com.byc.im.entity.Message;
import com.byc.im.service.IMService;
import com.byc.im.support.ChatGroup;
import com.byc.im.support.SocketChannelGroup;
import com.byc.im.support.UserGroup;
import com.byc.im.support.common.APPConfig;
import com.byc.im.support.pojo.Messages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;


/**
 * Created by baiyc
 * 2020/5/20/020 14:44
 * Description：
 */
@Slf4j
public class WebsocketHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    private APPConfig config;

    private IMService service;

    public WebsocketHandler(APPConfig config,IMService service) {
        this.config = config;
        this.service = service;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("收到消息："+msg);
        if (msg instanceof FullHttpRequest){
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.info("客户端加入连接："+ctx.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        log.info("客户端断开连接："+ctx.channel());
        SocketChannelGroup.removeChannel(ctx.channel());
        UserGroup.removeUser(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        log.info("服务端收到：" + request);
        //消息分发
        WebSocketDispatcher(ctx,request);
    }
    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws JsonProcessingException {
        //要求Upgrade为websocket，过滤掉get/Post等http请求
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        String uri = req.uri();
        System.out.println();
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                config.getWebsocket().getAddr(), null, false);
        handshaker = wsFactory.newHandshaker(req);
        log.info("地址："+config.getWebsocket().getAddr());
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            //绑定到用户
            UserGroup.bindUserChannel(Long.valueOf(uri.substring(uri.indexOf("?") + 1)), ctx.channel().id().toString());
            handshaker.handshake(ctx.channel(), req);
            SocketChannelGroup.addChannel(ctx.channel());
        }
    }
    /**
     * 拒绝不合法的http请求，并返回
     * */
    private void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 消息分发器
     * **/
    private void WebSocketDispatcher(ChannelHandlerContext ctx, String request){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Messages messages = objectMapper.readValue(request.getBytes(), Messages.class);
            String type=messages.getPayLoad().getType();
            if (type.equals(Messages.PVP) && p2p(messages,request)){
                Object data = messages.getPayLoad().getData();
                service.saveMessage(Message.From.USER,data,messages.getPayLoad());
            }else if (type.equals(Messages.PVG)){
                Long targetId = messages.getPayLoad().getTarget();
                Long sourceId = messages.getPayLoad().getSource();
                String channelId = UserGroup.search(sourceId).getChannelId();
                io.netty.channel.group.ChannelGroup channels = ChatGroup.getChatGroup(targetId);
                if (channels!=null){
                    Channel channel = SocketChannelGroup.findChannel(channelId);
                    //不给自己发消息
                    channels.remove(channel);
                    channels.writeAndFlush(new TextWebSocketFrame(request));
                    channels.add(channel);
                }else {
                    throw new IOException();
                }
            }else if(type.equals(Messages.FRIEND_APPLY)){
                p2p(messages,request);
            }
        } catch (IOException e) {
            ctx.writeAndFlush(new TextWebSocketFrame(Messages.err("消息发送失败！[ "+request+" ]").toString()));
        }
    }

    /**一对一消息*/
    private boolean p2p(Messages messages, String request){
        Long targetId = messages.getPayLoad().getTarget();
        String channelId = UserGroup.search(targetId).getChannelId();
        Channel channel = SocketChannelGroup.findChannel(channelId);
        if (channel!=null){
            //保存消息
            channel.writeAndFlush(new TextWebSocketFrame(request));
            return true;
        }
        return false;
    }
}
