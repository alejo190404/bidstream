package com.bidstream.subastas.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.bidstream.subastas.kafka.dto.BidCreateEvent;
import com.bidstream.subastas.service.AuctionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SubastaKafkaListeners {

    private static final Logger log = LoggerFactory.getLogger(SubastaKafkaListeners.class);

    private final AuctionService auctionService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    public SubastaKafkaListeners(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @KafkaListener(topics = "bid.create", groupId = "pujas-group")
    public void onBidCreate(@Payload Map<String, Object> message) {
        try {
            // 1. Manually convert the HashMap into your DTO
            BidCreateEvent event = objectMapper.convertValue(message, BidCreateEvent.class);

            log.info("Received at listener bid.create: auctionId={}, bidderId={}, amount={}", event.getAuctionId(), event.getBidderId(), event.getAmount());

            // 2. Publish the internal event
            eventPublisher.publishEvent(event); 
        
        } catch (Exception e) {
            log.error("Error processing or converting bid.create", e);
        }
    }
}
