package com.bidstream.subastas.kafka.dto;

public class StreamAddUserEvent {

    private String auctionId;
    private String userId;

    public String getAuctionId() { return auctionId; }
    public void setAuctionId(String auctionId) { this.auctionId = auctionId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
