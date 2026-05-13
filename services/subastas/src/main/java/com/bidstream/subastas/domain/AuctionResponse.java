package com.bidstream.subastas.domain;

import java.math.BigDecimal;

public class AuctionResponse {

    private String id;
    private String title;
    private String description;
    private String auctioneerId;
    private String status;
    private String streamRoomId;
    private String startTime;
    private String endTime;
    private BigDecimal currentPrice;
    private int bidCount;

    public static AuctionResponse from(Auction a) {
        AuctionResponse r = new AuctionResponse();
        r.id = a.getId();
        r.title = a.getTitle();
        r.description = a.getDescription();
        r.auctioneerId = a.getAuctioneerId();
        r.status = a.getStatus() != null ? a.getStatus().name() : "SCHEDULED";
        r.streamRoomId = a.getStreamRoomId();
        r.startTime = a.getStartTime() != null ? a.getStartTime().toString() : null;
        r.endTime = a.getEndTime() != null ? a.getEndTime().toString() : null;
        r.bidCount = a.getBids() != null ? a.getBids().size() : 0;
        r.currentPrice = (a.getBids() == null || a.getBids().isEmpty())
                ? BigDecimal.ZERO
                : a.getBids().get(0).getAmount();
        return r;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuctioneerId() { return auctioneerId; }
    public String getStatus() { return status; }
    public String getStreamRoomId() { return streamRoomId; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public int getBidCount() { return bidCount; }
}
