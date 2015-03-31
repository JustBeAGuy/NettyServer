package com.hamstercoders.server;

import com.hamstercoders.server.entities.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import org.apache.log4j.Logger;


/**
 * Created by Администратор on 3/27/15.
 */
public class TrafficCounterHandler extends ChannelTrafficShapingHandler {

    private Request request;

    public TrafficCounterHandler(Request request) {
        super(1000);
        this.request = request;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        //start count throughput
        trafficCounter().start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        long sentBytes = trafficCounter().getRealWrittenBytes().get();
        long receivedBytes = trafficCounter().cumulativeReadBytes();

        //stop count throughput
        trafficCounter().stop();

        long throughput = trafficCounter.getRealWriteThroughput();

        //Set traffic statistics to Request
        request.setReceivedBytes(receivedBytes);
        request.setSentBytes(sentBytes);
        request.setThroughput(throughput);
    }

}
