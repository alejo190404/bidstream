package com.bidstream.subastas.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "bids")
@CompoundIndex(name = "auction_created", def = "{ 'auctionId': 1, 'createdAt': -1 }")
public class Bid {

    @Id
    private String id;
    private String auctionId;
    private String bidderId;
    private BigDecimal amount;
    private Instant createdAt = Instant.now();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAuctionId() { return auctionId; }
    public void setAuctionId(String auctionId) { this.auctionId = auctionId; }
    public String getBidderId() { return bidderId; }
    public void setBidderId(String bidderId) { this.bidderId = bidderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
