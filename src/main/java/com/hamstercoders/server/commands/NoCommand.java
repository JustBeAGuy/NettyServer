package com.hamstercoders.server.commands;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;

/**
 * Created by Администратор on 3/25/15.
 */
public class NoCommand extends Command {

    @Override
    public void execute(ChannelHandlerContext ctx, HttpRequest req, HashMap<String, String> param) {
        sendResp(ctx, createResponsePage());
    }

    @Override
    protected String createResponsePage() {
        StringBuilder res = new StringBuilder();
        StringBuilder text = new StringBuilder("Page Not Found");

        res.append(
                wrapping("html",
                        wrapping("body",
                                wrapping("H2", text))));

        return res.toString();
    }
}
