package com.hamstercoders.server.commands;

import com.hamstercoders.server.ServerStatistics;
import com.hamstercoders.server.entities.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Администратор on 3/25/15.
 */
public class StatusCommand extends Command {

    private static ServerStatistics serverStatistics = ServerStatistics.getInstance();

    @Override
    public void execute(ChannelHandlerContext ctx, HttpRequest req, HashMap<String, String> param) {
        sendResp(ctx, createResponsePage());
    }

    @Override
    protected String createResponsePage() {
        StringBuilder res;

        StringBuilder numOfReq = new StringBuilder("Number Of Requests - "
                + serverStatistics.getNumOfReq().toString());
        StringBuilder numOfUniqueReq = new StringBuilder("Number Of Unique Requests - "
                + serverStatistics.getNumberOfUniqueReq().toString());
        StringBuilder uniqueReq = createUniqueReqTable();
        StringBuilder redirects = createRedirectsTable();
        StringBuilder openConnections = new StringBuilder("Open Connections - "
                + serverStatistics.getOpenConnections().toString());
        StringBuilder lastRequests = createLastReqTable();
        StringBuilder css = createCss();

        res = wrapping("html",
                new StringBuilder(wrapping("head", css)).append(
                        wrapping("body",
                                wrapping("H2", numOfReq).append(
                                        wrapping("H2", numOfUniqueReq)).append(
                                        wrapping("H3", uniqueReq)).append(
                                        wrapping("H3", redirects)).append(
                                        wrapping("H2", openConnections)).append(
                                        wrapping("H3", lastRequests))
                        )
                )
        );

        return res.toString();
    }

    private StringBuilder createCss() {
        StringBuilder css = new StringBuilder();
        css.append(wrapping("style",
                new StringBuilder("table, td, th {" +
                                " border: 1px solid black; " +
                            "}" +
                            "table {" + 
                                "border-collapse: collapse;" +
                            "}")));
        return css;
    }

    private StringBuilder createUniqueReqTable() {
        StringBuilder uniqueReq = new StringBuilder();
        //wrapping th
        uniqueReq.append(wrapping("tr",
                wrapping("th", new StringBuilder("IP")).append(
                        wrapping("th", new StringBuilder("Last Req Time"))).append(
                        wrapping("th",new StringBuilder("Number of Req")))
        ));

        for(Map.Entry<String,ServerStatistics.UniqueRequest> entry : serverStatistics.getUniqueReq().entrySet()) {
            StringBuilder td = new StringBuilder();
            //wrapping of Table Data
            td.append(wrapping("td", new StringBuilder(entry.getKey())))
                    .append(wrapping("td", new StringBuilder(new StringBuilder(String.valueOf(entry.getValue().getLastTimeReq())))))
                    .append(wrapping("td", new StringBuilder(new StringBuilder(String.valueOf(entry.getValue().getNumOfReq())))));
            //wrapping of Table Row
            uniqueReq.append(wrapping("tr", td));
        }

        return wrapping("table", uniqueReq);
    }

    private StringBuilder createLastReqTable() {
        StringBuilder lastRequests = new StringBuilder();
        //wrapping th
        lastRequests.append(wrapping("tr",
                        wrapping("th", new StringBuilder("SourceIp")).append(
                                wrapping("th", new StringBuilder("URI"))).append(
                                wrapping("th", new StringBuilder("TimeStamp"))).append(
                                wrapping("th", new StringBuilder("SentBytes"))).append(
                                wrapping("th", new StringBuilder("ReceivedBytes"))).append(
                        wrapping("th",new StringBuilder("Throughput bytes/sec")))
        ));

        for(Request lastReq : serverStatistics.getLastRequests()) {
            StringBuilder td = new StringBuilder();
            //wrapping of Table Data
            td.append(wrapping("td", new StringBuilder(lastReq.getSourceIp())))
                    .append(wrapping("td", new StringBuilder(lastReq.getUri())))
                    .append(wrapping("td", new StringBuilder(lastReq.getTimestamp().toString())))
                    .append(wrapping("td", new StringBuilder(String.valueOf(lastReq.getSentBytes()))))
                    .append(wrapping("td", new StringBuilder(String.valueOf(lastReq.getReceivedBytes()))))
                    .append(wrapping("td", new StringBuilder(String.valueOf(lastReq.getThroughput()))));
            //wrapping of Table Row
            lastRequests.append(wrapping("tr", td));
        }

        return wrapping("table", lastRequests);
    }

    private StringBuilder createRedirectsTable() {
        StringBuilder redirects = new StringBuilder();

        //wrapping th
        redirects.append(wrapping("tr",
                wrapping("th", new StringBuilder("URL")).append(
                        wrapping("th",new StringBuilder("Number of Redirects")))
        ));

        for(Map.Entry<String, AtomicInteger> entry : serverStatistics.getRedirects().entrySet()) {
            StringBuilder td = new StringBuilder();
            //wrapping of Table Data
            td.append(wrapping("td", new StringBuilder(entry.getKey())))
                    .append(wrapping("td", new StringBuilder(new StringBuilder(String.valueOf(entry.getValue())))));
            //wrapping of Table Row
            redirects.append(wrapping("tr", td));
        }

        return wrapping("table", redirects);
    }


}
