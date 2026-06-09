import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Email } from '../models/email';


@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private baseUrl = 'http://localhost:8088/api/v1/auth/emails';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  constructor(private http: HttpClient) { }

  fetchAndSaveEmails(accountId: number): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/fetch-and-save/${accountId}`);
  }

  searchEmails(accountId: number, sender?: string, subject?: string, startDate?: Date, endDate?: Date): Observable<Email[]> {
    const params = {
      sender: sender || '',
      subject: subject || '',
      startDate: startDate?.toISOString() || '',
      endDate: endDate?.toISOString() || ''
    };
    return this.http.get<Email[]>(`${this.baseUrl}/search/${accountId}`, { params });
  }

  searchByEmail(email: string, accountId: number): Observable<Email[]> {
    return this.http.get<Email[]>(`${this.baseUrl}/search-by-email/${accountId}`, { params: { email } });
  }

  deleteEmail(emailId: number, accountId: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/${emailId}/${accountId}`,this.httpOptions);
  }

  getAllEmailsByAccountId(accountId: number): Observable<Email[]> {
    return this.http.get<Email[]>(`${this.baseUrl}/all/${accountId}`,this.httpOptions);
  }

  archiveEmail(emailId: number, accountId: number): Observable<string> {
    return this.http.put<string>(`${this.baseUrl}/archive/${emailId}/${accountId}`, this.httpOptions);
  }

  searchByDomain(domainName: string, accountId: number): Observable<Email[]> {
    return this.http.get<Email[]>(`${this.baseUrl}/search-by-domain/${accountId}`, { params: { domainName } },);
  }
  getEmailById(emailId: number): Observable<Email> {
    return this.http.get<Email>(`${this.baseUrl}/${emailId}`, this.httpOptions);
  }
  
}
