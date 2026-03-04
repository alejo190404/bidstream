package com.bidstream.subastas.domain;

import java.math.BigDecimal;
import java.time.Instant;

public class BidRecord {

    private String bidderId;
    private BigDecimal amount;
    private Instant createdAt;

    public BidRecord() {}

    public BidRecord(String bidderId, BigDecimal amount, Instant createdAt) {
        this.bidderId = bidderId;
        this.amount = amount;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public String getBidderId() { return bidderId; }
    public void setBidderId(String bidderId) { this.bidderId = bidderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
