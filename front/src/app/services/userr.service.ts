import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { USER } from './regres';
import { ResetPasswordRequest } from '../models/ResetPasswordRequest';
import { PasswordRequest } from '../models/passwordRequest';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8088/api';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  getUsers(): Observable<USER[]> {
    return this.http.get<USER[]>(`${this.baseUrl}/ListUser`, this.httpOptions);
  }

  getUserInfo(id: number): Observable<USER> {
    return this.http.get<USER>(`${this.baseUrl}/UserInfo?id=${id}`, this.httpOptions);
  }

  findByEmail(email: string): Observable<USER> {
    return this.http.get<USER>(`${this.baseUrl}/email/${email}`, this.httpOptions);
  }

  addUser(user:USER): Observable<USER> {
    return this.http.post<USER>(`${this.baseUrl}/AddUser`, user, this.httpOptions);
  }

  removeUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/removeUser/${id}`, this.httpOptions);
  }

  resetPassword(request: ResetPasswordRequest): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/reset-password`, request, this.httpOptions);
  }

  forgetPassword(email: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/forget_password`, email, this.httpOptions);
  }

  checkEmailUnique(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-email-uniqueness?email=${email}`, this.httpOptions);
  }

 /* updateUser(id: number, user: USER): Observable<USER> {
    return this.http.put<USER>(`${this.baseUrl}/modifieruser/${id}`, user, this.httpOptions) .pipe(
      catchError(this.handleError)
    );
}*/
updateUser(id: number, user: USER): Observable<USER[]> {
  console.log(user);
  return this.http.put<USER[]>(`${this.baseUrl}/modifieruser/${id}`, user);
}
private handleError(error: HttpErrorResponse) {
  let errorMessage = '';
  if (error.error instanceof ErrorEvent) {
    // Client-side error
    errorMessage = `Error: ${error.error.message}`;
  } else {
    // Server-side error
    errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
  }
  return throwError(errorMessage);
}
  

  updatePassword(id: number, request: PasswordRequest): Observable<USER> {
    return this.http.put<USER>(`${this.baseUrl}/updatepassword/${id}`, request, this.httpOptions);
  }

  searchByPropertyName(username: string): Observable<USER[]> {
    return this.http.get<USER[]>(`${this.baseUrl}/user/searchh?username=${username}`, this.httpOptions);
  }
}
