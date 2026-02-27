package com.bidstream.subastas.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bidstream.subastas.domain.BidRecord;
import com.bidstream.subastas.kafka.dto.BidCreateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.spy.memcached.MemcachedClient;

@Service
public class PujaCacheService {

    private static final String KEY_PREFIX = "auction:";
    private static final int TTL_SECONDS = (int) TimeUnit.HOURS.toSeconds(86400); //Amount of seconds in a day. Allows for 24 hour auctions.
    private static final Logger log = LoggerFactory.getLogger(PujaCacheService.class);

    private final MemcachedClient memcachedClient;
    private final ObjectMapper objectMapper;

    public PujaCacheService(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Optional<BidRecord> getHighestBid(String auctionId) {
        String key = KEY_PREFIX + auctionId;
        Object value = memcachedClient.get(key);
        if (value == null) return Optional.empty();
        try {
            BidRecord record = objectMapper.readValue(value.toString(), BidRecord.class);
            return Optional.of(record);
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize highest bid for auction {}", auctionId, e);
            return Optional.empty();
        }
    }

    @Async("bidEventExecutor")
    @EventListener
    public void setHighestBidIfGreater(BidCreateEvent event) {
        log.info("Received async at service bid.create: auctionId={}, bidderId={}, amount={}", event.getAuctionId(), event.getBidderId(), event.getAmount());
        
        // String key = KEY_PREFIX + auctionId;
        // BidRecord newRecord = new BidRecord(bidderId, amount, createdAt);
        // try {
        //     String json = objectMapper.writeValueAsString(newRecord);
        //     Optional<BidRecord> current = getHighestBid(auctionId);
        //     if (current.isPresent() && current.get().getAmount().compareTo(amount) >= 0) {
        //         return false; // not higher
        //     }
        //     memcachedClient.set(key, TTL_SECONDS, json);
        //     return true;
        // } catch (JsonProcessingException e) {
        //     log.error("Failed to serialize bid for auction {}", auctionId, e);
        //     return false;
        // }
    }
}
