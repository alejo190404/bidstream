package com.bidstream.subastas.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bidstream.subastas.domain.Auction;
import com.bidstream.subastas.domain.AuctionResponse;
import com.bidstream.subastas.domain.BidRecord;
import com.bidstream.subastas.domain.CreateAuctionRequest;
import com.bidstream.subastas.kafka.dto.PersistBidEvent;
import com.bidstream.subastas.repository.AuctionRepository;
import com.bidstream.subastas.utils.StringUtils;

@Service
public class AuctionService {

    private static final Logger log = LoggerFactory.getLogger(AuctionService.class);

    private final AuctionRepository auctionRepository;
    private final PujaCacheService pujaCacheService;

    @org.springframework.beans.factory.annotation.Value("${pagos-usuarios.url:http://localhost:8082}")
    private String pagosUsuariosUrl;

    public AuctionService(AuctionRepository auctionRepository,
            PujaCacheService pujaCacheService) {
        this.auctionRepository = auctionRepository;
        this.pujaCacheService = pujaCacheService;
    }

    public List<AuctionResponse> getAllAuctions() {
        return auctionRepository.findAll().stream().map(AuctionResponse::from).toList();
    }

    public Auction createAuction(CreateAuctionRequest request) {
        Auction auction = new Auction();
        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setAuctioneerId(request.getAuctioneerId());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setStatus(Auction.AuctionStatus.SCHEDULED);
        auction.setCreatedAt(Instant.now());
        auction.setStreamRoomId("bidstream-" + StringUtils.generateRandomString(12)); // TODO: Revisar si vale la pena
                                                                                      // dejarlo al azar
        return auctionRepository.save(auction);
    }

    public void startAuction(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if (auction.isPresent()) {
            Auction found = auction.get();
            found.setStatus(Auction.AuctionStatus.LIVE);
            auctionRepository.save(found);
        } else {
            log.warn("Auction with id {} not found", auctionId);
        }
    }

    @Async("persistingEventExecutor")
    @EventListener
    public void persistBid(PersistBidEvent event) {
        Optional<Auction> auction = auctionRepository.findById(event.getAuctionId());
        if (auction.isPresent()) {
            Auction found = auction.get();
            // 1. Create the new BidRecord from the event data
            BidRecord newBid = new BidRecord(
                    event.getBidderId(),
                    event.getAmount(),
                    Instant.now());

            // 2. Add to the FIRST position (index 0)
            found.getBids().add(0, newBid);

            // 3. Persist the update
            auctionRepository.save(found);

            log.info("New bid of {} added to top of auction {}", event.getAmount(), event.getAuctionId());

            auctionRepository.save(found);
        } else {
            log.warn("Auction with id {} not found", event.getAuctionId());
        }
    }
}
