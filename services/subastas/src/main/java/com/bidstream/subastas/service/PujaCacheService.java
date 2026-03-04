package com.bidstream.subastas.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bidstream.subastas.domain.BidRecord;
import com.bidstream.subastas.kafka.dto.BidCreateEvent;
import com.bidstream.subastas.kafka.dto.PersistBidEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.spy.memcached.MemcachedClient;

@Service
public class PujaCacheService {

    private static final String KEY_PREFIX = "auction:";
    private static final int TTL_SECONDS = 86400; //Amount of seconds in a day. Allows for 24 hour auctions.
    private static final Logger log = LoggerFactory.getLogger(PujaCacheService.class);

    private final MemcachedClient memcachedClient;
    private final ObjectMapper objectMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public PujaCacheService(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void initPuja(String auctionId, String bidderId, BigDecimal amount){
        String key = KEY_PREFIX + auctionId;
        BidRecord initialRecord = new BidRecord(bidderId, amount, Instant.now());
        try {
            String json = objectMapper.writeValueAsString(initialRecord);
            memcachedClient.set(key, TTL_SECONDS, json);
            log.info("Created initial bid cache for key {} with bidder {} and amount {}", key, bidderId, amount);
        } catch (JsonProcessingException e) {
            log.error("Failed to initialize highest bid for auction {}", auctionId, e);
        }
    }

    public Optional<BidRecord> getHighestBid(String key) {
        log.info("Trying to retrieve from cache the key {}", key);
        Object value = memcachedClient.get(key);
        if (value == null) {
            log.info("Cache retreived null");
            return Optional.empty();
        }
        try {
            log.info("Cache did not retreive null");
            BidRecord record = objectMapper.readValue(value.toString(), BidRecord.class);
            return Optional.of(record);
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize highest bid for auction {}", key, e);
            return Optional.empty();
        }
    }

    @Async("bidEventExecutor")
    @EventListener
    public void setHighestBidIfGreater(BidCreateEvent event) {
        String auctionId = event.getAuctionId();
        String key = KEY_PREFIX + auctionId;
        BidRecord newRecord = new BidRecord(event.getBidderId(), event.getAmount(), Instant.now());
        try {
            String json = objectMapper.writeValueAsString(newRecord);
            Optional<BidRecord> current = getHighestBid(key);
            if (current.isPresent() && current.get().getAmount().compareTo(event.getAmount()) < 0) {
                //Update cache
                memcachedClient.set(key, TTL_SECONDS, json);
                
                // Send event to persist the highest bid
                eventPublisher.publishEvent(new PersistBidEvent(auctionId, event.getBidderId(), event.getAmount()));
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize bid for auction {}", auctionId, e);
        }
    }
}
