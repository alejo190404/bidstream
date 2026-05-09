import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User, SignInRequest } from '../models/user.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly _currentUser = signal<User | null>(null);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly currentUser = this._currentUser.asReadonly();
  readonly isAuthenticated = computed(() => this._currentUser() !== null);
  readonly loading = this._loading.asReadonly();
  readonly error = this._error.asReadonly();

  signIn(req: SignInRequest): void {
    this._loading.set(true);
    this._error.set(null);

    // TODO: replace with Keycloak token exchange:
    // this.http.post<{ access_token: string }>(`${environment.keycloakBase}/protocol/openid-connect/token`, body)
    //   .subscribe({ next: (res) => { /* decode JWT, set user */ }, error: () => this._error.set('Invalid credentials') });
    setTimeout(() => {
      const initials = req.email.slice(0, 2).toUpperCase();
      this._currentUser.set({
        id: 'mock_user_001',
        username: req.email.split('@')[0],
        email: req.email,
        avatarInitials: initials,
      });
      this._loading.set(false);
      this.router.navigate(['/']);
    }, 800);
  }

  signOut(): void {
    // TODO: call Keycloak logout endpoint
    this._currentUser.set(null);
    this.router.navigate(['/']);
  }
}
