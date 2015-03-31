package com.hamstercoders.server.commands;

import com.hamstercoders.server.ServerStatistics;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.log4j.Logger;

import java.util.HashMap;

import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;

/**
 * Created by Администратор on 3/25/15.
 */
public class RedirectCommand extends Command {

    private static final String URL = "url";

    @Override
    public void execute(ChannelHandlerContext ctx, HttpRequest req, HashMap<String, String> param) {
        if(param.containsKey(URL)) {
            //add redirect url to statistics
            ServerStatistics.getInstance().addRedirect(param.get(URL));
            //redirect
            redirect(ctx, param.get(URL));
        } else {
            sendResp(ctx, createResponsePage());
        }
    }

    @Override
    protected String createResponsePage() {
        StringBuilder res = new StringBuilder();
        StringBuilder text = new StringBuilder("No URL Parameter");

        res.append(
                wrapping("html",
                        wrapping("body",
                                wrapping("H2", text))));

        return res.toString();
    }

    protected void redirect(ChannelHandlerContext ctx, String url) {
        //Creating Response
        FullHttpResponse response
                = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        //setting headers
//        Logger.getLogger(RedirectCommand.class).info(url);
        response.headers().set(LOCATION, url);

        //send(write) response
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
