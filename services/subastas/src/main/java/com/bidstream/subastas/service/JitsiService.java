package com.bidstream.subastas.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Builds Jitsi room URL for an auction.
 * MVP: deterministic room id (bidstream-{auctionId}) using public meet.jit.si.
 * Optional: when JITSI_APP_ID and JITSI_APP_SECRET are set, could call Jitsi as a Service API to create meetings.
 */
@Service
public class JitsiService {

    private static final Logger log = LoggerFactory.getLogger(JitsiService.class);

    @Value("${jitsi.meet-url:https://meet.jit.si}")
    private String meetUrl;

    @Value("${jitsi.app-id:}")
    private String appId;

    @Value("${jitsi.app-secret:}")
    private String appSecret;

    private final PujaCacheService pujaCacheService;

    public JitsiService(PujaCacheService pujaCacheService) {
        this.pujaCacheService = pujaCacheService;
    }

    /**
     * Full URL for the client to join the stream.
     */
    public String startAuction(String auctioneerId, String auctionId) {
        pujaCacheService.initPuja(auctionId, auctioneerId, BigDecimal.ZERO);
        String base = meetUrl.endsWith("/") ? meetUrl : meetUrl + "/";
        return base + auctionId;
    }
}
