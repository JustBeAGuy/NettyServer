package com.hamstercoders.server.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Администратор on 3/29/15.
 */
public class Request implements Serializable {

    private String sourceIp;
    private String uri;
    private Date timestamp;
    private long sentBytes;
    private long receivedBytes;
    private long throughput;

    public Request() {
    }

    public Request(String sourceIp, String uri, Date timestamp, long sentBytes, long receivedBytes, long throughput) {
        this.sourceIp = sourceIp;
        this.uri = uri;
        this.timestamp = timestamp;
        this.sentBytes = sentBytes;
        this.receivedBytes = receivedBytes;
        this.throughput = throughput;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public long getThroughput() {
        return throughput;
    }

    public void setThroughput(long throughput) {
        this.throughput = throughput;
    }
}
