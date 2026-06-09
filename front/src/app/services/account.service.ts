import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Account } from '../models/account';
import { accountt } from './regres';


@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private baseUrl = 'http://localhost:8088/api/v1/auth/';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  constructor(private http: HttpClient) { }

  createAccount(userId: number, account: Account): Observable<Account> {
    return this.http.post<Account>(`${this.baseUrl}create/${userId}`, account,this.httpOptions);
  }

  getAccountById(id: number): Observable<Account> {
    return this.http.get<Account>(`${this.baseUrl}${id}`,this.httpOptions);
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}all`,this.httpOptions);
  }

  updateAccountt(userId: number, id: number, updatedAccount: accountt): Observable<accountt> {
    console.log(updatedAccount)
    return this.http.put<accountt>(`${this.baseUrl}update/${userId}/${id}`, updatedAccount,this.httpOptions);
  }

  deleteAccount(userId: number, id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}delete/${userId}/${id}`,this.httpOptions);
  }

  getAccountsByUserId(userId: number): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}user/${userId}/accounts`,this.httpOptions);
  }

  getAccountByIdAndUserId(userId: number, id: number): Observable<Account> {
    return this.http.get<Account>(`${this.baseUrl}user/${userId}/account/${id}`,this.httpOptions);
  }
  updateAccount(userId: number, id: number, updatedAccount: Account): Observable<accountt> {
    // Create a copy of the updatedAccount object without the 'user' and 'id' fields
    const { user, id: accountId, ...accountWithoutUserAndId } = updatedAccount;
    
    console.log(accountWithoutUserAndId);
    return this.http.put<Account>(`${this.baseUrl}update/${userId}/${id}`, accountWithoutUserAndId, this.httpOptions);
  }
}
