package com.bidstream.subastas.domain;

public class CreateAuctionRequest {
    private String auctioneerId;
    private String title;
    private String description;
    private java.time.Instant startTime;
    private java.time.Instant endTime;

    public String getAuctioneerId() {
        return auctioneerId;
    }

    public void setAuctioneerId(String auctioneerId) {
        this.auctioneerId = auctioneerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.time.Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(java.time.Instant startTime) {
        this.startTime = startTime;
    }

    public java.time.Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(java.time.Instant endTime) {
        this.endTime = endTime;
    }
}
