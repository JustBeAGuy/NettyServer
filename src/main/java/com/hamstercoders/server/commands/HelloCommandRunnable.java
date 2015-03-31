package com.hamstercoders.server.commands;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Администратор on 3/27/15.
 */
public class HelloCommandRunnable implements Runnable {

    //Channel Handler Context
    private ChannelHandlerContext ctx;

    //HelloCommand
    private HelloCommand helloCommand;

    public HelloCommandRunnable(ChannelHandlerContext ctx, HelloCommand helloCommand) {
        this.ctx = ctx;
        this.helloCommand = helloCommand;
    }

    @Override
    public void run() {
        //Create response page and Send
        helloCommand.sendResp(ctx, helloCommand.createResponsePage());
    }
}
