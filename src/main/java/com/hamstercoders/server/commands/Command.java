package com.hamstercoders.server.commands;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import java.util.HashMap;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Администратор on 3/25/15.
 */
public abstract class Command {

    public abstract void execute(ChannelHandlerContext ctx, HttpRequest req, HashMap<String, String> param);

    protected abstract String createResponsePage();

    protected void sendResp(ChannelHandlerContext ctx, String resp) {
        //Creating Response
        FullHttpResponse response
                = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(resp, CharsetUtil.UTF_8));
        //setting headers
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        //send(write) response
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    //wrapping body with a tag
    protected final StringBuilder wrapping(String tag, StringBuilder body) {
        StringBuilder res = new StringBuilder();

        res.append("<" + tag + ">").append(body).append("</" + tag + ">");

        return res;
    }
}
