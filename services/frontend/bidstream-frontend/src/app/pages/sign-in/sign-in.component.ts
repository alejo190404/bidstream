import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="page">
      <div class="card">
        <div class="logo-row">
          <div class="logo-mark">
            <div class="bar" style="height:8px;opacity:0.45"></div>
            <div class="bar" style="height:14px;opacity:0.7"></div>
            <div class="bar" style="height:20px"></div>
            <div class="dot"></div>
          </div>
          <span class="logo-text">Bid<span class="logo-accent">Stream</span></span>
        </div>

        <h1 class="heading">Sign In</h1>
        <p class="sub">Welcome back. Enter your credentials to continue.</p>

        @if (auth.error()) {
          <div class="error-banner">{{ auth.error() }}</div>
        }

        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form">
          <div class="field">
            <label class="label" for="email">Email</label>
            <input
              id="email"
              type="email"
              class="input"
              placeholder="you@example.com"
              formControlName="email"
              [class.input-error]="submitted() && form.get('email')?.invalid" />
            @if (submitted() && form.get('email')?.hasError('required')) {
              <span class="field-error">Email is required</span>
            }
            @if (submitted() && form.get('email')?.hasError('email')) {
              <span class="field-error">Enter a valid email address</span>
            }
          </div>

          <div class="field">
            <label class="label" for="password">Password</label>
            <input
              id="password"
              type="password"
              class="input"
              placeholder="••••••••"
              formControlName="password"
              [class.input-error]="submitted() && form.get('password')?.invalid" />
            @if (submitted() && form.get('password')?.hasError('required')) {
              <span class="field-error">Password is required</span>
            }
          </div>

          <button type="submit" class="submit-btn" [disabled]="auth.loading()">
            @if (auth.loading()) {
              Signing in…
            } @else {
              Sign In
            }
          </button>
        </form>

        <p class="footer-text">
          Don't have an account?
          <a routerLink="/" class="link">Sign up</a>
        </p>
      </div>
    </div>
  `,
  styleUrl: './sign-in.component.scss',
})
export class SignInComponent {
  readonly auth = inject(AuthService);
  private readonly fb = inject(FormBuilder);

  readonly submitted = signal(false);

  readonly form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  onSubmit(): void {
    this.submitted.set(true);
    if (this.form.invalid) return;
    this.auth.signIn({
      email: this.form.value.email!,
      password: this.form.value.password!,
    });
  }
}
