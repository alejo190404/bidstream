package com.bidstream.gateway.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bidstream.gateway.service.SubastaKafkaProducer;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final SubastaKafkaProducer kafkaProducer;

    @Value("${gateway.subastas-url:http://localhost:8081}")
    private String subastasUrl;

    @Value("${gateway.pagos-usuarios-url:http://localhost:8082}")
    private String pagosUsuariosUrl;

    public GatewayController(SubastaKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    //Funciones para actuar como Proxy. TODO: Definir si vale la pena el comportamiento

    private String sub(String path) {
        String base = subastasUrl.endsWith("/") ? subastasUrl.substring(0, subastasUrl.length() - 1) : subastasUrl;
        return base + path;
    }

    private String pagos(String path) {
        String base = pagosUsuariosUrl.endsWith("/") ? pagosUsuariosUrl.substring(0, pagosUsuariosUrl.length() - 1) : pagosUsuariosUrl;
        return base + path;
    }

    private ResponseEntity<?> proxyGet(String url, String auth) {
        HttpHeaders headers = new HttpHeaders();
        if (auth != null) headers.set("Authorization", auth);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    private ResponseEntity<?> proxyPost(String url, Object body, String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (auth != null) headers.set("Authorization", auth);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // -------------------------- Controller enpdoints -----------------------------------

    @GetMapping("/auctions")
    public ResponseEntity<?> listAuctions(@RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return
        return ResponseEntity.ok("/auctions works fine");
        
    }

    @GetMapping("/auctions/{id}")
    public ResponseEntity<?> getAuction(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return
        return ResponseEntity.ok("/auctions works fine");
    }

    @GetMapping("/auctions/{id}/highest-bid")
    public ResponseEntity<?> getHighestBid(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return
        return ResponseEntity.ok("/auctions works fine");
    }

    @GetMapping("/auctions/{id}/stream-url")
    public ResponseEntity<?> getStreamUrl(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return
        return ResponseEntity.ok("/auctions works fine");
    }

    @PostMapping("/auctions")
    public ResponseEntity<?> createAuction(@RequestBody Map<String, Object> body, @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return and test publishing to kafka
        return ResponseEntity.ok("/auctions works fine");
    }

    @PostMapping("/auctions/{id}/start-streaming")
    public ResponseEntity<?> startStreaming(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return and test publishing to kafka
        return ResponseEntity.ok("/auctions works fine");
    }

    @PostMapping("/auctions/{id}/end")
    public ResponseEntity<?> endAuction(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return and test publishing to kafka
        return ResponseEntity.ok("/auctions works fine");
    }

    /** Place bid: Gateway publishes to Kafka (Subastas consumes). */
    @PostMapping("/auctions/{id}/bids")
    public ResponseEntity<?> placeBid(@PathVariable String id,
                                       @RequestBody Map<String, Object> body,
                                       @AuthenticationPrincipal Jwt jwt) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return and test publishing to kafka
        return ResponseEntity.ok("/auctions works fine");
    }

    // --- Pagos-Usuarios proxy ---

    @GetMapping("/payments")
    public ResponseEntity<?> listPayments(@RequestHeader(value = "Authorization", required = false) String auth,
                                          @RequestParam(required = false) String userId) {
        String path = "/api/payments" + (userId != null ? "?userId=" + userId : "");
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return and test publishing to kafka
        return ResponseEntity.ok("/auctions works fine");
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable String paymentId,
                                        @RequestHeader(value = "Authorization", required = false) String auth) {
        //Return a string that says "/auctions works fine" and a 200 OK. TODO: Implement correct return and test publishing to kafka
        return ResponseEntity.ok("/auctions works fine");
    }
}
