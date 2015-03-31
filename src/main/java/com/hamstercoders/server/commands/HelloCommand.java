package com.hamstercoders.server.commands;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Администратор on 3/25/15.
 */
public class HelloCommand extends Command {

    //Time Delay
    private static final long TIME_DELAY = 10;
    //Time Unit
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    @Override
    public void execute(ChannelHandlerContext ctx, HttpRequest req, HashMap<String, String> param) {
        //Creating runnable HelloCommand
        Runnable runnable = new HelloCommandRunnable(ctx, this);
        //Adding to the schedule for Delay
        ctx.executor().schedule(runnable, TIME_DELAY, TIME_UNIT);
    }

    @Override
    protected String createResponsePage() {
        StringBuilder res = new StringBuilder();
        StringBuilder text = new StringBuilder("Hello World");

        res.append(
                wrapping("html",
                    wrapping("body",
                            wrapping("H2", text))));

        return res.toString();
    }

}
