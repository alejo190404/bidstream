import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent),
  },
  {
    path: 'sign-in',
    loadComponent: () => import('./pages/sign-in/sign-in.component').then(m => m.SignInComponent),
  },
  {
    path: 'create',
    loadComponent: () => import('./pages/create-auction/create-auction.component').then(m => m.CreateAuctionComponent),
  },
  { path: '**', redirectTo: '' },
];
