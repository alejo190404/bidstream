package com.bidstream.subastas.web;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidstream.subastas.domain.AuctionResponse;
import com.bidstream.subastas.domain.CreateAuctionRequest;
import com.bidstream.subastas.domain.StartAuctionRequest;
import com.bidstream.subastas.service.AuctionService;
import com.bidstream.subastas.service.JitsiService;

@RestController
@RequestMapping("/api/auctions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuctionController {

    private final AuctionService auctionService;
    private final JitsiService jitsiService;

    public AuctionController(AuctionService auctionService, JitsiService jitsiService) {
        this.auctionService = auctionService;
        this.jitsiService = jitsiService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuctionResponse>> getAllAuctions() {
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @PostMapping(path = "/start", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String startAuction(@RequestBody StartAuctionRequest request) {
        auctionService.startAuction(request.getAuctionId());
        return jitsiService.startAuction(
                request.getAuctioneerId(),
                request.getAuctionId()
        );
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuctionResponse> createSubasta(@RequestBody CreateAuctionRequest request) {
        return ResponseEntity.status(201).body(AuctionResponse.from(auctionService.createAuction(request)));
    }
}
