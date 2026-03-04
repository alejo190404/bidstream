package com.bidstream.subastas.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auctions")
public class Auction {

    @Id
    private String id= UUID.randomUUID().toString();;
    private String title;
    private String description;
    @Indexed
    private String auctioneerId;
    private AuctionStatus status = AuctionStatus.SCHEDULED;
    private String streamRoomId;  // Jitsi room id (e.g. bidstream-{id})
    private Instant startTime;
    private Instant endTime;
    private Instant createdAt = Instant.now();
    private List<BidRecord> bids = new ArrayList<>();

    public enum AuctionStatus {
        SCHEDULED,
        LIVE,
        ENDED
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAuctioneerId() { return auctioneerId; }
    public void setAuctioneerId(String auctioneerId) { this.auctioneerId = auctioneerId; }
    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }
    public String getStreamRoomId() { return streamRoomId; }
    public void setStreamRoomId(String streamRoomId) { this.streamRoomId = streamRoomId; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public List<BidRecord> getBids() { return bids; }
    public void setBids(List<BidRecord> bids) { this.bids = bids; }
}
