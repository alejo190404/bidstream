import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <header class="header">
      <div class="inner">
        <a class="logo" routerLink="/">
          <div class="logo-mark">
            <div class="bar" style="height:8px;opacity:0.45"></div>
            <div class="bar" style="height:14px;opacity:0.7"></div>
            <div class="bar" style="height:20px"></div>
            <div class="dot"></div>
          </div>
          <span class="logo-text">Bid<span class="logo-accent">Stream</span></span>
        </a>

        <nav class="nav">
          <a routerLink="/" routerLinkActive="nav-active" [routerLinkActiveOptions]="{exact:true}" class="nav-btn">Browse</a>
        </nav>

        <div class="right">
          @if (auth.isAuthenticated()) {
            <span class="username">{{ auth.currentUser()?.username }}</span>
            <button class="ghost-btn" (click)="auth.signOut()">Sign Out</button>
            <div class="avatar">{{ auth.currentUser()?.avatarInitials }}</div>
          } @else {
            <a routerLink="/sign-in" class="ghost-btn">Sign In</a>
            <a routerLink="/create" class="create-btn">+ Create Auction</a>
          }
        </div>
      </div>
    </header>
  `,
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  readonly auth = inject(AuthService);
}
