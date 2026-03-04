package com.bidstream.gateway.domain;

public class StartAuctionRequest {
    private String auctioneerId;
    private String auctionId;
    private java.time.Instant endTime;

    public String getAuctioneerId() {
        return auctioneerId;
    }

    public void setAuctioneerId(String auctioneerId) {
        this.auctioneerId = auctioneerId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public java.time.Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(java.time.Instant endTime) {
        this.endTime = endTime;
    }
}