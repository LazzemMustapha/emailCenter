import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArchivedEmail } from '../models/ArchivedEmail ';


@Injectable({
  providedIn: 'root'
})
export class ArchivedEmailService {
  private baseUrl = 'http://localhost:8088/api/v1/auth/emails';

  constructor(private http: HttpClient) { }

  archiverEmailsSelonRegles(): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/archiver/emails`, {});
  }

  getAllArchivedEmails(accountId: number): Observable<ArchivedEmail[]> {
    const params = new HttpParams().set('accountId', accountId.toString());
    return this.http.get<ArchivedEmail[]>(`${this.baseUrl}/all`, { params });
  }

  getArchivedEmailsByDateRange(accountId: number, startDate: string, endDate: string): Observable<ArchivedEmail[]> {
    const params = new HttpParams()
      .set('accountId', accountId.toString())
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<ArchivedEmail[]>(`${this.baseUrl}/archived-emails`, { params });
  }

  getArchivedEmailsBySenderAndDateRange(accountId: number, sender: string, startDate: string, endDate: string): Observable<ArchivedEmail[]> {
    const params = new HttpParams()
      .set('accountId', accountId.toString())
      .set('sender', sender)
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<ArchivedEmail[]>(`${this.baseUrl}/archived-emails-by-sender`, { params });
  }

  deleteArchivedEmail(accountId: number, id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/archived-emails/${accountId}/${id}`);
  }

  deleteArchivedEmailsOlderThanOneYear(accountId: number): Observable<string> {
    const params = new HttpParams().set('accountId', accountId.toString());
    return this.http.delete<string>(`${this.baseUrl}/archived-emails/delete-old`, { params });
  }
}
