import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Auction, AuctionStatus, CreateAuctionRequest } from '../models/auction.model';
import { environment } from '../../../environments/environment';

interface BackendAuction {
  id: string;
  title: string;
  description: string;
  auctioneerId: string;
  status: 'SCHEDULED' | 'LIVE' | 'ENDED';
  streamRoomId?: string;
  startTime: string;
  endTime: string;
  currentPrice: number;
  bidCount: number;
}

const STATUS_MAP: Record<string, AuctionStatus> = {
  SCHEDULED: 'upcoming',
  LIVE: 'live',
  ENDED: 'ended',
};

@Injectable({ providedIn: 'root' })
export class AuctionService {
  private readonly http = inject(HttpClient);

  private readonly _auctions = signal<Auction[]>([]);
  readonly auctions = this._auctions.asReadonly();
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

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
    this.loading.set(true);
    this.error.set(null);
    this.http
      .get<BackendAuction[]>(`${environment.apiBase}/api/auctions`)
      .subscribe({
        next: (data) => {
          this._auctions.set(data.map(b => this.mapBackendAuction(b)));
          this.loading.set(false);
        },
        error: (err) => {
          console.error('Failed to load auctions', err);
          this.error.set('Failed to load auctions. Please try again.');
          this.loading.set(false);
        },
      });
  }

  getById(id: string): Auction | undefined {
    return this._auctions().find(a => a.id === id);
  }

  create(req: CreateAuctionRequest): void {
    this.loading.set(true);
    this.error.set(null);
    this.http
      .post<BackendAuction>(`${environment.apiBase}/api/auctions/create-auction`, req)
      .subscribe({
        next: (created) => {
          this._auctions.update(list => [this.mapBackendAuction(created), ...list]);
          this.loading.set(false);
        },
        error: (err) => {
          console.error('Failed to create auction', err);
          this.error.set('Failed to create auction. Please try again.');
          this.loading.set(false);
        },
      });
  }

  private mapBackendAuction(b: BackendAuction): Auction {
    return {
      id: b.id,
      title: b.title,
      description: b.description,
      auctioneerId: b.auctioneerId,
      sellerName: b.auctioneerId,
      status: STATUS_MAP[b.status] ?? 'upcoming',
      startPrice: 0,
      currentPrice: b.currentPrice ?? 0,
      bidCount: b.bidCount ?? 0,
      watcherCount: 0,
      category: 'Other',
      startTime: b.startTime,
      endTime: b.endTime,
      streamRoomId: b.streamRoomId,
    };
  }
}
