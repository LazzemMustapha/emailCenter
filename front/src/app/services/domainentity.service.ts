import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { DomainEntity } from '../models/domain-entity';

@Injectable({
  providedIn: 'root'
})
export class DomainEntityService {
  private baseUrl = 'http://localhost:8088/api/v1/auth';

  constructor(private http: HttpClient) { }

  createDomain(domainEntity: DomainEntity): Observable<DomainEntity> {
    return this.http.post<DomainEntity>(`${this.baseUrl}/domains`, domainEntity);
  }

  getAllDomainEntities(): Observable<DomainEntity[]> {
    return this.http.get<DomainEntity[]>(`${this.baseUrl}/allDomain`);
  }

  deleteDomainEntity(id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/domain/${id}`);
  }
  getAllDomainEntitiesByAccountId(accountId: number): Observable<DomainEntity[]> {
    return this.http.get<DomainEntity[]>(`${this.baseUrl}/${accountId}/domain-entities`).pipe(
      catchError(error => {
        console.error('Error fetching domain entities', error);
        return throwError(error);
      })
    );
  }
}
