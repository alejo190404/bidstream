package com.bidstream.gateway.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bidstream.gateway.domain.StartAuctionRequest;

@Service
public class SubastaService {
    private final RestTemplate restTemplate;

    public SubastaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createAuction() {
        return "Todo melo caramelo";
    }

    public String startAuction(StartAuctionRequest request, String subastasUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StartAuctionRequest> entity = new HttpEntity<>(request, headers);
        String url = subastasUrl + "/api/auctions/start";
        // Change Object.class to String.class to bypass JSON parsing
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Log this to your console!
        System.out.println("Response Body: " + response.getBody());
        return response.getBody().toString();
    }
}