import { Injectable } from '@angular/core';


import { HttpClient, HttpHeaders } from '@angular/common/http';

import {USER} from "./regres";
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/operators';
import { of } from 'rxjs';
const AUTH_API = 'http://localhost:8088/api/v1/auth';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) { }
  userLogin(credentials:USER): Observable<any> {
    return this.http.post(AUTH_API + '/authenticate', {
      email: credentials.email,
     password: credentials.password
    }, httpOptions);
  }
  register(user: USER): Observable<any> {
    return this.http.post(AUTH_API + '/register', {
      
      username: user.username,
      email: user.email,
      password: user.password,
      
    }, httpOptions);
  }
  register1(user: USER): Observable<any> {
    return this.http.post(AUTH_API + '/registerclient', {
     
      username: user.username,
      email: user.email,
      password: user.password,
      
    }, httpOptions);
  }
  updatepassword(user: USER): Observable<any> {
    return this.http.post(AUTH_API + '/updatepassword/${id}', {
      username: user.username,
      email: user.email,
      password: user.password
    }, httpOptions);
  }
 logout(): Observable<any> {
  const authToken = localStorage.getItem('jwtToken'); // ← uniformisé

  if (authToken) {
    httpOptions.headers = httpOptions.headers.set('Authorization', 'Bearer ' + authToken);
  }

  return this.http.post(AUTH_API + '/logout', {}, httpOptions).pipe(
    tap(() => {
      localStorage.removeItem('jwtToken');   // ← nettoyer après logout
      localStorage.removeItem('auth-user');
    })
  );
}
  
  
}
