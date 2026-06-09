import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArchivingRule } from '../models/archiving-rule';


@Injectable({
  providedIn: 'root'
})
export class ArchivingRuleService {
 
  private baseUrl = 'http://localhost:8088/api/archivingRules';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }
  getArchivingRules(): Observable<ArchivingRule[]> {
    return this.http.get<ArchivingRule[]>(this.baseUrl,this.httpOptions);
  }
  getAllArchivingRulesByAccountId(accountId: number): Observable<ArchivingRule[]> {
    return this.http.get<ArchivingRule[]>(`${this.baseUrl}/account/${accountId}`,this.httpOptions);
  }
  

  addArchivingRule(archivingRule: ArchivingRule): Observable<ArchivingRule> {
    return this.http.post<ArchivingRule>(this.baseUrl, archivingRule,this.httpOptions);
  }

  updateArchivingRule(id: number, archivingRuleDetails: ArchivingRule): Observable<ArchivingRule> {
    // Make a deep copy of the object to avoid mutating the original one
    const copy = JSON.parse(JSON.stringify(archivingRuleDetails));
    // Remove the user field from the account
    delete copy.account.user;

    return this.http.put<ArchivingRule>(`${this.baseUrl}/${id}`,copy ,this.httpOptions);
  }
 
}
