export interface User {
  id: string;
  username: string;
  email: string;
  avatarInitials: string;
}

export interface SignInRequest {
  email: string;
  password: string;
}
