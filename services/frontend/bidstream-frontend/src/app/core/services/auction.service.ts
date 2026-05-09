import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Auction, AuctionStatus, CreateAuctionRequest } from '../models/auction.model';
import { environment } from '../../../environments/environment';

const MOCK_AUCTIONS: Auction[] = [
  {
    id: '1',
    title: 'Leica M6 Film Camera',
    description: 'Pristine condition, rarely used. Includes original case and strap.',
    auctioneerId: 'seller_001',
    sellerName: 'vintage_optics',
    status: 'live',
    startPrice: 500,
    currentPrice: 1248,
    buyNow: 2000,
    bidCount: 38,
    watcherCount: 142,
    category: 'Cameras & Photography',
    startTime: new Date(Date.now() - 3600000).toISOString(),
    endTime: new Date(Date.now() + 247000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#0D1F3C,#0A2040)',
    recentBids: [
      { bidderId: 'user_9841', amount: 1248, createdAt: new Date(Date.now() - 2000).toISOString() },
      { bidderId: 'user_2210', amount: 1200, createdAt: new Date(Date.now() - 18000).toISOString() },
    ],
  },
  {
    id: '2',
    title: 'Rolex Submariner 2023',
    description: 'Full set, unworn. Papers, box, card all included.',
    auctioneerId: 'seller_002',
    sellerName: 'luxury_time_co',
    status: 'live',
    startPrice: 6000,
    currentPrice: 8400,
    buyNow: 12000,
    bidCount: 21,
    watcherCount: 89,
    category: 'Watches',
    startTime: new Date(Date.now() - 7200000).toISOString(),
    endTime: new Date(Date.now() + 1823000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#1A0D2E,#2A1040)',
  },
  {
    id: '3',
    title: 'Original Banksy Print',
    description: 'Authenticated print with certificate. Edition 7/50.',
    auctioneerId: 'seller_003',
    sellerName: 'art_vault',
    status: 'upcoming',
    startPrice: 3000,
    currentPrice: 3200,
    bidCount: 0,
    watcherCount: 0,
    category: 'Art',
    startTime: new Date(Date.now() + 8040000).toISOString(),
    endTime: new Date(Date.now() + 11640000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#1F0D0A,#3A1810)',
  },
  {
    id: '4',
    title: 'Nike Air Jordan 1 OG',
    description: 'Deadstock, size US11. Never worn, original box.',
    auctioneerId: 'seller_004',
    sellerName: 'sneaker_vault',
    status: 'live',
    startPrice: 200,
    currentPrice: 420,
    buyNow: 600,
    bidCount: 14,
    watcherCount: 67,
    category: 'Fashion',
    startTime: new Date(Date.now() - 1800000).toISOString(),
    endTime: new Date(Date.now() + 540000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#1A1000,#2A2010)',
  },
  {
    id: '5',
    title: 'Hasselblad 500C/M',
    description: 'Classic medium format film camera. Exceptional condition.',
    auctioneerId: 'seller_005',
    sellerName: 'pro_cameras',
    status: 'upcoming',
    startPrice: 2000,
    currentPrice: 2100,
    bidCount: 0,
    watcherCount: 0,
    category: 'Cameras & Photography',
    startTime: new Date(Date.now() + 20400000).toISOString(),
    endTime: new Date(Date.now() + 24000000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#0D1A20,#0A2030)',
  },
  {
    id: '6',
    title: 'Fender Stratocaster 1972',
    description: 'All original hardware. Natural relic. Case included.',
    auctioneerId: 'seller_006',
    sellerName: 'riff_house',
    status: 'ended',
    startPrice: 3500,
    currentPrice: 4750,
    bidCount: 22,
    watcherCount: 0,
    category: 'Music',
    startTime: new Date(Date.now() - 86400000).toISOString(),
    endTime: new Date(Date.now() - 3600000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#1A0D0A,#2A1A10)',
  },
  {
    id: '7',
    title: 'Porsche 911 (1985)',
    description: 'Numbers matching 3.2 Carrera. Full service history.',
    auctioneerId: 'seller_007',
    sellerName: 'classic_motors',
    status: 'upcoming',
    startPrice: 40000,
    currentPrice: 48000,
    bidCount: 0,
    watcherCount: 0,
    category: 'Vehicles',
    startTime: new Date(Date.now() + 86400000).toISOString(),
    endTime: new Date(Date.now() + 172800000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#0D0D1A,#101020)',
  },
  {
    id: '8',
    title: 'Apple-1 Replica Kit',
    description: 'Full-build replica PCB with period-correct components.',
    auctioneerId: 'seller_008',
    sellerName: 'retro_tech',
    status: 'ended',
    startPrice: 600,
    currentPrice: 890,
    bidCount: 9,
    watcherCount: 0,
    category: 'Electronics',
    startTime: new Date(Date.now() - 172800000).toISOString(),
    endTime: new Date(Date.now() - 86400000).toISOString(),
    cardGradient: 'linear-gradient(135deg,#0D1A0D,#0A200A)',
  },
];

@Injectable({ providedIn: 'root' })
export class AuctionService {
  private readonly http = inject(HttpClient);

  private readonly _auctions = signal<Auction[]>([]);
  readonly auctions = this._auctions.asReadonly();

  readonly liveAuctions = computed(() =>
    this._auctions().filter(a => a.status === 'live')
  );

  readonly featuredAuction = computed(() =>
    this._auctions().find(a => a.status === 'live') ?? null
  );

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    // TODO: replace with HTTP call:
    // this.http.get<Auction[]>(`${environment.apiBase}/api/auctions`)
    //   .subscribe(data => this._auctions.set(data));
    this._auctions.set(MOCK_AUCTIONS);
  }

  getById(id: string): Auction | undefined {
    return this._auctions().find(a => a.id === id);
  }

  create(req: CreateAuctionRequest): void {
    // TODO: replace with HTTP call:
    // this.http.post<Auction>(`${environment.apiBase}/api/auctions/create-auction`, req)
    //   .subscribe(created => this._auctions.update(list => [created, ...list]));
    const mock: Auction = {
      id: crypto.randomUUID(),
      title: req.title,
      description: req.description,
      auctioneerId: req.auctioneerId,
      sellerName: 'you',
      status: 'upcoming' as AuctionStatus,
      startPrice: req.startPrice,
      currentPrice: req.startPrice,
      buyNow: req.buyNow,
      bidCount: 0,
      watcherCount: 0,
      category: req.category,
      startTime: req.startTime,
      endTime: req.endTime,
    };
    this._auctions.update(list => [mock, ...list]);
  }
}
