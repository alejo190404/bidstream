package com.bidstream.gateway.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubastaKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SubastaKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAuctionCreate(String title, String description, String auctioneerId, Instant endTime) {
        Map<String, Object> event = new HashMap<>();
        event.put("title", title);
        event.put("description", description);
        event.put("auctioneerId", auctioneerId);
        event.put("endTime", endTime != null ? endTime.toString() : null);
        kafkaTemplate.send("auction.create", event);
    }

    public void sendAuctionStartStreaming(String auctionId) {
        Map<String, String> event = new HashMap<>();
        event.put("auctionId", auctionId);
        kafkaTemplate.send("auction.start-streaming", event);
    }

    public void sendAuctionEnd(String auctionId) {
        Map<String, String> event = new HashMap<>();
        event.put("auctionId", auctionId);
        kafkaTemplate.send("auction.end", event);
    }

    public void sendBidCreate(String auctionId, String bidderId, BigDecimal amount) {
        Map<String, Object> event = new HashMap<>();
        event.put("auctionId", auctionId);
        event.put("bidderId", bidderId);
        event.put("amount", amount);
        kafkaTemplate.send("bid.create", event);
    }

    public void sendStreamAddUser(String auctionId, String userId) {
        Map<String, String> event = new HashMap<>();
        event.put("auctionId", auctionId);
        event.put("userId", userId);
        kafkaTemplate.send("stream.add-user", event);
    }
}
