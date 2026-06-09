import { Injectable } from '@angular/core';
import {USER, account} from "./regres";
import { UserService } from './user.service';
import { Account } from '../models/account';
const TOKEN_KEY = 'jwtToken';
const USER_KEY = 'auth-user';
const FIRST_LOGIN_KEY = 'first-login';
const GLOBAL_FIRST_LOGIN_KEY = 'global-first-login';
@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  constructor(private clientservice : UserService) { }
  //_id:string
  signOut() {
    window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.removeItem(USER_KEY);
  }
  public saveToken(jwtToken: string) {
    //window.localStorage.removeItem(TOKEN_KEY);
    window.localStorage.setItem(TOKEN_KEY, jwtToken);
  }
  public getToken(): string {
    // @ts-ignore
    return localStorage.getItem(TOKEN_KEY);
  }
  public getemail(): account[]|any {
    // @ts-ignore
    const user = window.localStorage.getItem(USER_KEY);
    const email = this.getUser().accounts;
return email 
  }
  getAccountId(): number | null {
    const accountId = localStorage.getItem('accountId');
    return accountId ? Number(accountId) : null;
  }
  public saveUser(user:USER) {
    window.localStorage.removeItem(USER_KEY);
    const _id=this.clientservice.findemail(user.email)

    window.localStorage.setItem(USER_KEY, JSON.stringify(user));

  }
  public getUser(): any {
    const user = window.localStorage.getItem(USER_KEY);
    if (user) {

      return JSON.parse(user);

    }
    return {};
  }
  public getUserId(): any {
    const id = this.getUser().id;


      return id;


  }
  public getUserRole(): any {
    const role = window.localStorage.getItem(USER_KEY);
    if (role) {

      return role;

    }
   

  }
  /*public setFirstLogin(): void {
    localStorage.setItem(FIRST_LOGIN_KEY, 'false');
  }

  public isFirstLogin(): boolean {
    return localStorage.getItem(FIRST_LOGIN_KEY) === null;
  }*/
  public setFirstLogin(userId: number): void {
    localStorage.setItem(`first-login-${userId}`, 'false');
  }

  public isFirstLogin(userId: number): boolean {
    return localStorage.getItem(`first-login-${userId}`) === null;
  }
  /*public roleMatch(allowedRoles):boolean{
    var isMatch = false;
    var userRoles: string[] = JSON.parse(this.getUser().role);
    allowedRoles.forEach(element => {
      if (userRoles.indexOf(element) > -1) {
        isMatch = true;
        return false;
      }
    });
    return isMatch;
  }*/}
