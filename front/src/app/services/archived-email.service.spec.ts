import { TestBed } from '@angular/core/testing';

import { ArchivedEmailService } from './archived-email.service';

describe('ArchivedEmailService', () => {
  let service: ArchivedEmailService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArchivedEmailService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
