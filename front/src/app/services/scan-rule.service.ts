import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ScanRule } from '../models/scan-rule';

@Injectable({
  providedIn: 'root'
})
export class ScanRuleService {
  private baseUrl = 'http://localhost:8088/api/scan-rules';

  // Définition des options HTTP avec les en-têtes appropriés
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  addScanRule(scanRule: ScanRule): Observable<ScanRule> {
    return this.http.post<ScanRule>(this.baseUrl, scanRule, this.httpOptions);
  }

  updateScanRule(id: number, scanRule: ScanRule): Observable<ScanRule> {
    return this.http.put<ScanRule>(`${this.baseUrl}/${id}`, scanRule, this.httpOptions);
  }

  deleteScanRule(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, this.httpOptions);
  }

  getAllScanRules(): Observable<ScanRule[]> {
    return this.http.get<ScanRule[]>(this.baseUrl, this.httpOptions);
  }
}
