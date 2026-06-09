import { TestBed } from '@angular/core/testing';

import { DomainentityService } from './domainentity.service';

describe('DomainentityService', () => {
  let service: DomainentityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DomainentityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
