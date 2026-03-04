package com.bidstream.subastas.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bidstream.subastas.domain.Auction;

public interface AuctionRepository extends MongoRepository<Auction, String> {

    List<Auction> findByStatus(Auction.AuctionStatus status);

    List<Auction> findByAuctioneerId(String auctioneerId);

    List<Auction> findByStreamRoomId(String streamRoomId);
}
