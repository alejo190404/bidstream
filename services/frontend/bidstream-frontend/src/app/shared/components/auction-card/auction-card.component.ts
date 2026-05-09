import { Component, input } from '@angular/core';
import { Auction } from '../../../core/models/auction.model';

@Component({
  selector: 'app-auction-card',
  standalone: true,
  template: `
    <div class="card" [class.card-ended]="auction().status === 'ended'">
      <div class="thumb">
        <div class="thumb-bg" [style.background]="auction().cardGradient || 'linear-gradient(135deg,#0D1F3C,#152035)'">
          <span class="thumb-icon">{{ categoryIcon(auction().category) }}</span>
        </div>
        @if (auction().status === 'live') {
          <span class="badge badge-live">
            <span class="live-dot"></span>LIVE
          </span>
          @if (auction().watcherCount) {
            <span class="badge badge-watchers">👁 {{ auction().watcherCount }}</span>
          }
        }
        @if (auction().status === 'upcoming') {
          <span class="badge badge-upcoming">⏱ Upcoming</span>
        }
        @if (auction().status === 'ended') {
          <span class="badge badge-ended">ENDED</span>
        }
      </div>

      <div class="body">
        <div class="title">{{ auction().title }}</div>
        <div class="meta">
          <span class="seller">{{ auction().sellerName }}</span>
          <span class="bid-count">{{ auction().bidCount }} bids</span>
        </div>
        <div class="footer">
          <div>
            <div class="price-label">{{ auction().status === 'ended' ? 'Sold for' : 'Current bid' }}</div>
            <div class="price" [class.price-live]="auction().status === 'live'" [class.price-ended]="auction().status === 'ended'">
              {{ '$' + auction().currentPrice.toLocaleString() }}
            </div>
          </div>
          @if (auction().status === 'live') {
            <button class="bid-btn">Bid Now</button>
          }
        </div>
      </div>
    </div>
  `,
  styleUrl: './auction-card.component.scss',
})
export class AuctionCardComponent {
  readonly auction = input.required<Auction>();

  categoryIcon(category: string): string {
    const icons: Record<string, string> = {
      'Cameras & Photography': '📷',
      'Watches': '⌚',
      'Art': '🎨',
      'Fashion': '👟',
      'Music': '🎸',
      'Vehicles': '🏎️',
      'Electronics': '💻',
      'Collectibles': '🏆',
    };
    return icons[category] ?? '📦';
  }
}
