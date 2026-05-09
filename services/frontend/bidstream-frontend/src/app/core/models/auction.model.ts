export type AuctionStatus = 'live' | 'upcoming' | 'ended';

export interface BidRecord {
  bidderId: string;
  amount: number;
  createdAt: string;
}

export interface Auction {
  id: string;
  title: string;
  description: string;
  auctioneerId: string;
  sellerName: string;
  status: AuctionStatus;
  startPrice: number;
  currentPrice: number;
  buyNow?: number;
  bidCount: number;
  watcherCount: number;
  category: string;
  startTime: string;
  endTime: string;
  streamRoomId?: string;
  cardGradient?: string;
  recentBids?: BidRecord[];
}

export interface CreateAuctionRequest {
  title: string;
  description: string;
  category: string;
  startPrice: number;
  buyNow?: number;
  durationMinutes: number;
  enableStream: boolean;
  startTime: string;
  endTime: string;
  auctioneerId: string;
}
