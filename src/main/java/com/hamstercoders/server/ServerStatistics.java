package com.hamstercoders.server;

import com.hamstercoders.server.entities.Request;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Администратор on 3/29/15.
 */
public class ServerStatistics {

//    - общее количество запросов
//
//    - количество уникальных запросов (по одному на IP)
//
//    - счетчик запросов на каждый IP в виде таблицы с колонкам и IP,
//    кол-во запросов, время последнего запроса
//
//    - количество переадресаций по url'ам  в виде таблицы, с колонками
//    url, кол-во переадресация
//
//    - количество соединений, открытых в данный момент
//
//    - в виде таблицы лог из 16 последних обработанных соединений, колонки
//    src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)

    private static final ServerStatistics INSTANCE = new ServerStatistics();
    private static final int NUM_OF_LAST_CONNECTIONS = 16;

    private AtomicInteger numOfReq = new AtomicInteger();
    private AtomicInteger openConnections = new AtomicInteger();
    private AtomicInteger numOfUniqueReq = new AtomicInteger();
    private Map<String, AtomicInteger> redirects = new ConcurrentHashMap<String, AtomicInteger>();
    private Map<String, UniqueRequest> uniqueReq = new ConcurrentHashMap<String, UniqueRequest>();
    private ConcurrentLinkedDeque<Request> lastRequests = new ConcurrentLinkedDeque();

    public static ServerStatistics getInstance() {
        return INSTANCE;
    }

    public void increaseNumOfReq() {
        this.numOfReq.incrementAndGet();
    }

    public Integer getNumOfReq() {
        return this.numOfReq.intValue();
    }

    public void incrementOpenConnections() {
        this.openConnections.incrementAndGet();
    }

    public void decrementOpenConnections() {
        this.openConnections.decrementAndGet();
    }

    public Integer getOpenConnections() {
        return this.openConnections.intValue();
    }

    public Integer getNumberOfUniqueReq() {
        return this.numOfUniqueReq.intValue();
    }

    public void addRedirect(String url) {
        //checking if URL is in the Map and increment
        if(redirects.containsKey(url)) {
            //if yes, increase
            redirects.get(url).incrementAndGet();
        } else {
            //if no, add one
            redirects.put(url, new AtomicInteger(1));
        }
    }

    public Map<String, AtomicInteger> getRedirects() {
        return this.redirects;
    }

    public void addUniqueReq(String ip, Date time) {
        //checking if IP is in the Map
        if(uniqueReq.containsKey(ip)) {
            //if yes, increase and updateTime
            uniqueReq.get(ip).incrementNumOfReq(time);
        } else {
            //if no, add one
            uniqueReq.put(ip, new UniqueRequest(time));
            //increment numOfUniqueReq;
            incrementNumOfUniqueReq();
        }

    }

    private void incrementNumOfUniqueReq() {
        this.numOfUniqueReq.incrementAndGet();
    }

    public Map<String, UniqueRequest> getUniqueReq() {
        return this.uniqueReq;
    }

    public void addLastRequest(Request request) {
        //checking for size
        if(lastRequests.size() >= NUM_OF_LAST_CONNECTIONS) {
            //Poll last
            lastRequests.pollLast();
            //Offer first request
            lastRequests.offerFirst(request);
        } else {
            lastRequests.offerFirst(request);
        }
    }

    public ConcurrentLinkedDeque<Request> getLastRequests() {
        return this.lastRequests;
    }

    public void setOpenConnections(int size) {
        openConnections.set(size);
    }

    public class UniqueRequest {

        private AtomicInteger numOfReq = new AtomicInteger();

        private volatile Date lastTimeReq;

        public UniqueRequest() {}

        public UniqueRequest(Date time) {
            this.lastTimeReq = time;
            this.numOfReq.incrementAndGet();
        }

        public void incrementNumOfReq(Date time) {
            this.numOfReq.incrementAndGet();
            updateLastTime(time);
        }

        private void updateLastTime(Date time) {
            this.lastTimeReq = time;
        }

        public Date getLastTimeReq() {
            return this.lastTimeReq;
        }

        public Integer getNumOfReq() {
            return this.numOfReq.intValue();
        }
    }

}
