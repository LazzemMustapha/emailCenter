import { Injectable } from '@angular/core';
import { USER, account } from "./regres";

const TOKEN_KEY = 'jwtToken';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() { } // ← supprimé UserService inutilisé

  signOut(): void {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.removeItem(USER_KEY);
  }

  public saveToken(jwtToken: string): void {
    window.localStorage.setItem(TOKEN_KEY, jwtToken);
  }

  public getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: USER): void {
    window.localStorage.removeItem(USER_KEY);
    window.localStorage.setItem(USER_KEY, JSON.stringify(user));
    // ← supprimé findemail() qui ne servait à rien
  }

  public getUser(): any {
    const user = window.localStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : {};
  }

  public getUserId(): any {
    return this.getUser().id;
  }

  public getemail(): account[] | any {
    return this.getUser().accounts;
  }

  public getAccountId(): number | null {
    const accountId = localStorage.getItem('accountId');
    return accountId ? Number(accountId) : null;
  }

  public setFirstLogin(userId: number): void {
    localStorage.setItem(`first-login-${userId}`, 'false');
  }

  public isFirstLogin(userId: number): boolean {
    return localStorage.getItem(`first-login-${userId}`) === null;
  }
}