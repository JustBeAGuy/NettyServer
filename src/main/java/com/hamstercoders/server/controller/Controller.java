package com.hamstercoders.server.controller;

import com.hamstercoders.server.commands.Command;
import com.hamstercoders.server.commands.CommandFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Администратор on 3/25/15.
 */
public class Controller {
    private static final Controller INSTANCE = new Controller();
    private static CommandFactory commandFactory = CommandFactory.getInstance();

    private Controller() {
    }

    public static Controller getInstance() {
        return INSTANCE;
    }

    private void processRequest(ChannelHandlerContext ctx, HttpRequest req, HashMap<String, String> param, String commandReq) {

        //Getting needed command
        Command command = commandFactory.getCommand(commandReq);

        //Execute command
        command.execute(ctx,req,param);
    }

    public void doGet(ChannelHandlerContext ctx, HttpRequest req) {

        //Setting Command
        String commandReq = getCommandFromURI(req.getUri());
//        commandReq = "redirect";

        //Parameters HashMap
        HashMap<String, String> param = new HashMap<String, String>();

        //Getting Parameters from URI
        Map<String, List<String>> params = new QueryStringDecoder(req.getUri()).parameters();
        for(Map.Entry entry : params.entrySet()) {

            String key = (String) entry.getKey();
            String value = "";

            List list = (List) entry.getValue();
            if(list.size() > 0) {
                value = (String) list.get(0);
            }

            //fill in param
            param.put(key, value);
        }

        //process request
        processRequest(ctx, req, param, commandReq);

    }

    private String getCommandFromURI(String uri){
        String res = "index";
        //Obtain Command from URI
        String[] splitedURI = uri.split("\\?");
        splitedURI = splitedURI[0].split("/");
        if(splitedURI.length > 0) {
            res = splitedURI[1];
        }
        return res;
    }

}
