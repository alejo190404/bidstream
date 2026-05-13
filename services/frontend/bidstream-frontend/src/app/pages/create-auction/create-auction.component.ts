import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuctionService } from '../../core/services/auction.service';

type DurationOption = '5' | '15' | '30' | '60' | '180' | 'custom';

const CATEGORIES = [
  'Electronics', 'Cameras & Photography', 'Watches', 'Art',
  'Collectibles', 'Fashion', 'Vehicles', 'Music', 'Other',
];

const DURATION_OPTIONS: { value: DurationOption; label: string }[] = [
  { value: '5', label: '5 min' },
  { value: '15', label: '15 min' },
  { value: '30', label: '30 min' },
  { value: '60', label: '1 hour' },
  { value: '180', label: '3 hours' },
  { value: 'custom', label: 'Custom' },
];

const STEPS = ['Details', 'Pricing', 'Schedule', 'Review'];

@Component({
  selector: 'app-create-auction',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="page">
      <div class="container">
        <!-- Header -->
        <div class="header">
          <button class="back-link" routerLink="/">← Back</button>
          <h2 class="heading">Create Auction</h2>
          <div class="step-dots">
            @for (s of steps; track s; let i = $index) {
              <div class="step-item">
                <div class="step-dot"
                  [class.step-active]="i === step()"
                  [class.step-done]="i < step()">
                </div>
                <span class="step-label" [class.step-label-active]="i === step()">{{ s }}</span>
                @if (i < steps.length - 1) {
                  <div class="step-line" [class.step-line-done]="i < step()"></div>
                }
              </div>
            }
          </div>
        </div>

        <!-- Step content -->
        <div class="card">
          @switch (step()) {
            @case (0) {
              <div class="fields">
                <div class="field">
                  <label class="label">Auction Title</label>
                  <input
                    class="input"
                    placeholder="e.g. Leica M6 Film Camera"
                    [value]="form().title"
                    (input)="set('title', $any($event.target).value)" />
                </div>
                <div class="field">
                  <label class="label">Category</label>
                  <select class="input" [value]="form().category" (change)="set('category', $any($event.target).value)">
                    @for (cat of categories; track cat) {
                      <option [value]="cat">{{ cat }}</option>
                    }
                  </select>
                </div>
                <div class="field">
                  <label class="label">Description</label>
                  <textarea
                    class="input textarea"
                    placeholder="Describe your item — condition, provenance, what's included…"
                    [value]="form().description"
                    (input)="set('description', $any($event.target).value)">
                  </textarea>
                </div>
              </div>
            }

            @case (1) {
              <div class="fields">
                <div class="field">
                  <label class="label">Starting Price</label>
                  <div class="input-prefix-wrap">
                    <span class="prefix">$</span>
                    <input
                      class="input input-prefixed"
                      type="number"
                      min="0"
                      placeholder="0.00"
                      [value]="form().startPrice"
                      (input)="set('startPrice', $any($event.target).value)" />
                  </div>
                  <span class="hint">The minimum first bid amount</span>
                </div>
                <div class="field">
                  <label class="label">Buy Now Price <span class="optional">(optional)</span></label>
                  <div class="input-prefix-wrap">
                    <span class="prefix">$</span>
                    <input
                      class="input input-prefixed"
                      type="number"
                      min="0"
                      placeholder="0.00"
                      [value]="form().buyNow"
                      (input)="set('buyNow', $any($event.target).value)" />
                  </div>
                  <span class="hint">Buyers can skip the auction and buy instantly</span>
                </div>
              </div>
            }

            @case (2) {
              <div class="fields">
                <div class="field">
                  <label class="label">Auction Duration</label>
                  <div class="duration-grid">
                    @for (opt of durationOptions; track opt.value) {
                      <button
                        class="duration-btn"
                        [class.duration-active]="form().duration === opt.value"
                        (click)="set('duration', opt.value)">
                        {{ opt.label }}
                      </button>
                    }
                  </div>
                </div>
                <div class="field">
                  <label class="label">Live Stream</label>
                  <div class="toggle-row">
                    <div class="toggle" [class.toggle-on]="form().enableStream" (click)="set('enableStream', !form().enableStream)">
                      <div class="toggle-thumb" [class.toggle-thumb-on]="form().enableStream"></div>
                    </div>
                    <span class="toggle-label">
                      {{ form().enableStream ? 'Stream enabled — go live when your auction starts' : 'No stream — bidders will see the listing only' }}
                    </span>
                  </div>
                </div>
              </div>
            }

            @case (3) {
              <div class="fields">
                <div class="review-card">
                  <div class="review-row"><span class="rl">Title</span><span class="rv">{{ form().title || '—' }}</span></div>
                  <div class="review-row"><span class="rl">Category</span><span class="rv">{{ form().category }}</span></div>
                  <div class="review-row"><span class="rl">Start Price</span><span class="rv">{{ '$' + (form().startPrice || '0') }}</span></div>
                  <div class="review-row"><span class="rl">Buy Now</span><span class="rv">{{ form().buyNow ? '$' + form().buyNow : 'Not set' }}</span></div>
                  <div class="review-row"><span class="rl">Duration</span><span class="rv">{{ durationLabel() }}</span></div>
                  <div class="review-row review-row-last"><span class="rl">Live Stream</span><span class="rv">{{ form().enableStream ? 'Yes' : 'No' }}</span></div>
                </div>
                <button class="live-btn" (click)="onGoLive()">Go Live Now</button>
              </div>
            }
          }
        </div>

        <!-- Footer nav -->
        <div class="footer">
          @if (step() > 0) {
            <button class="prev-btn" (click)="prevStep()">Back</button>
          }
          @if (step() < 3) {
            <button
              class="next-btn"
              [class.next-disabled]="!canProceed()"
              (click)="nextStep()">
              Continue →
            </button>
          }
        </div>
      </div>
    </div>
  `,
  styleUrl: './create-auction.component.scss',
})
export class CreateAuctionComponent {
  private readonly auctionService = inject(AuctionService);
  private readonly router = inject(Router);

  readonly steps = STEPS;
  readonly categories = CATEGORIES;
  readonly durationOptions = DURATION_OPTIONS;

  readonly step = signal(0);

  readonly form = signal({
    title: '',
    category: 'Electronics',
    description: '',
    startPrice: '',
    buyNow: '',
    duration: '60' as DurationOption,
    enableStream: true,
  });

  readonly canProceed = computed(() => {
    const f = this.form();
    return [
      f.title.trim().length > 2,
      f.startPrice.length > 0 && Number(f.startPrice) > 0,
      true,
      true,
    ][this.step()];
  });

  readonly durationLabel = computed(() => {
    const d = this.form().duration;
    return DURATION_OPTIONS.find(o => o.value === d)?.label ?? d;
  });

  set(key: string, value: any): void {
    this.form.update(f => ({ ...f, [key]: value }));
  }

  nextStep(): void {
    if (this.canProceed()) this.step.update(s => s + 1);
  }

  prevStep(): void {
    this.step.update(s => s - 1);
  }

  onGoLive(): void {
    const f = this.form();
    const durationMs = Number(f.duration) * 60 * 1000;
    const startTime = new Date().toISOString();
    const endTime = new Date(Date.now() + durationMs).toISOString();

    this.auctionService.create({
      title: f.title,
      description: f.description,
      category: f.category,
      startPrice: Number(f.startPrice),
      buyNow: f.buyNow ? Number(f.buyNow) : undefined,
      durationMinutes: Number(f.duration),
      enableStream: f.enableStream,
      startTime,
      endTime,
      auctioneerId: 'anonymous', // TODO: get from AuthService.currentUser().id
    });

    this.router.navigate(['/']);
  }
}
