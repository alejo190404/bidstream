package com.bidstream.subastas.kafka.dto;

import java.math.BigDecimal;

public class BidCreateEvent {

    private String auctionId;
    private String bidderId;
    private BigDecimal amount;

    public String getAuctionId() { return auctionId; }
    public void setAuctionId(String auctionId) { this.auctionId = auctionId; }
    public String getBidderId() { return bidderId; }
    public void setBidderId(String bidderId) { this.bidderId = bidderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public BidCreateEvent(String auctionId, String bidderId, BigDecimal amount) {
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
    }  
}
