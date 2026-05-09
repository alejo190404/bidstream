import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuctionService } from '../../core/services/auction.service';
import { AuctionCardComponent } from '../../shared/components/auction-card/auction-card.component';

type FilterTab = 'All' | 'Live' | 'Upcoming' | 'Ended';
const FILTER_TABS: FilterTab[] = ['All', 'Live', 'Upcoming', 'Ended'];

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, AuctionCardComponent],
  template: `
    <div class="page">
      <!-- Hero -->
      <section class="hero">
        <div class="hero-glow"></div>
        <div class="hero-content">
          <div class="hero-badge">
            <span class="hero-dot"></span>
            {{ liveCount() }} auction{{ liveCount() !== 1 ? 's' : '' }} live now
          </div>
          <h1 class="hero-heading">Bid Live.<br>Win Big.</h1>
          <p class="hero-sub">Watch live streams and place real-time bids on rare items from sellers worldwide.</p>
          <div class="hero-ctas">
            <button class="btn-primary" routerLink="/create">Watch Live →</button>
            <button class="btn-ghost" routerLink="/create">Start an Auction</button>
          </div>
        </div>

        @if (auctionService.featuredAuction(); as featured) {
          <div class="hero-card">
            <div class="hero-thumb" [style.background]="featured.cardGradient || 'linear-gradient(135deg,#0D1F3C,#0A2040)'">
              <span class="hero-thumb-icon">{{ categoryIcon(featured.category) }}</span>
            </div>
            <div class="hero-card-body">
              <div class="hero-live-pill">
                <span class="hero-live-dot"></span>
                LIVE · {{ featured.watcherCount }} watching
              </div>
              <div class="hero-card-title">{{ featured.title }}</div>
              <div class="hero-card-price">{{ '$' + featured.currentPrice.toLocaleString() + '.00' }}</div>
            </div>
          </div>
        }
      </section>

      <!-- Grid -->
      <section class="content">
        <div class="toolbar">
          <div class="filters">
            @for (tab of filterTabs; track tab) {
              <button
                class="filter-btn"
                [class.filter-active]="activeFilter() === tab"
                (click)="activeFilter.set(tab)">
                {{ tab }}
              </button>
            }
          </div>
          <span class="count">{{ filteredAuctions().length }} auctions</span>
        </div>

        @if (filteredAuctions().length > 0) {
          <div class="grid">
            @for (auction of filteredAuctions(); track auction.id) {
              <app-auction-card [auction]="auction" />
            }
          </div>
        } @else {
          <div class="empty-state">
            <span class="empty-icon">🔍</span>
            <p>No auctions yet. Start one.</p>
            <a routerLink="/create" class="btn-primary" style="display:inline-flex;margin-top:12px;">Create Auction</a>
          </div>
        }
      </section>
    </div>
  `,
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  readonly auctionService = inject(AuctionService);
  readonly filterTabs = FILTER_TABS;

  readonly activeFilter = signal<FilterTab>('All');

  readonly liveCount = computed(() =>
    this.auctionService.liveAuctions().length
  );

  readonly filteredAuctions = computed(() => {
    const filter = this.activeFilter();
    const all = this.auctionService.auctions();
    if (filter === 'All') return all;
    return all.filter(a => a.status === filter.toLowerCase());
  });

  categoryIcon(category: string): string {
    const icons: Record<string, string> = {
      'Cameras & Photography': '📷',
      'Watches': '⌚',
      'Art': '🎨',
      'Fashion': '👟',
      'Music': '🎸',
      'Vehicles': '🏎️',
      'Electronics': '💻',
    };
    return icons[category] ?? '📦';
  }
}
