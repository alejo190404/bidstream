package com.bidstream.subastas.config;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
public class SubastasConfig {

    @Value("${memcached.host:localhost}")
    private String memcachedHost;

    @Value("${memcached.port:11211}")
    private int memcachedPort;

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new MemcachedClient(new InetSocketAddress(memcachedHost, memcachedPort));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
