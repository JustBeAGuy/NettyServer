package com.hamstercoders.server;

import com.hamstercoders.server.controller.Controller;
import com.hamstercoders.server.entities.Request;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Created by Администратор on 3/25/15.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Controller controller = Controller.getInstance();
    private static ServerStatistics serverStatistics = ServerStatistics.getInstance();
    private Request request;

    public HttpServerHandler(Request request) {
        this.request = request;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest req) throws Exception {

        //set Request uri
        request.setUri(req.getUri());

        //checking for GET Method
        if(req.getMethod().equals(HttpMethod.GET)) {
            //Create and send response
            controller.doGet(ctx, req);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Logger.getLogger(HttpServerHandler.class).info("Channel Active");
        //Adding channel
        Channel incoming = ctx.channel();
        channels.add(incoming);
        //Set Open Connections
        serverStatistics.setOpenConnections(channels.size());

        //time and Ip
        Date timeStamp = new Date(System.currentTimeMillis());
        String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();

        //Set Request
        request.setSourceIp(ip);
        request.setTimestamp(timeStamp);

        //Increase number of Req and Unique
        serverStatistics.increaseNumOfReq();
        serverStatistics.addUniqueReq(ip, timeStamp);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Logger.getLogger(HttpServerHandler.class).info("Channel InActive");
        //Set Open Connections
        serverStatistics.setOpenConnections(channels.size());

        //Add Request to statistics
        serverStatistics.addLastRequest(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
