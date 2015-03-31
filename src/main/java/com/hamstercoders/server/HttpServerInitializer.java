package com.hamstercoders.server;

import com.hamstercoders.server.entities.Request;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.logging.Logger;

/**
 * Created by Администратор on 3/25/15.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final int MAX_CONTENT_LENGTH  = 1048576;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //getting pipeline from socket
        ChannelPipeline pipeline = socketChannel.pipeline();

        Request request = new Request();

        //Traffic handler, where we shaping statistics
        pipeline.addLast("traffic-counter", new TrafficCounterHandler(request));
        //Code and Decode Http Req/Resp
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("http-aggregator", new HttpObjectAggregator(MAX_CONTENT_LENGTH));
        //Handler
        pipeline.addLast("handler", new HttpServerHandler(request));
    }
}
