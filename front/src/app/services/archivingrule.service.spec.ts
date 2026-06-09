import { TestBed } from '@angular/core/testing';

import { ArchivingruleService } from './archivingrule.service';

describe('ArchivingruleService', () => {
  let service: ArchivingruleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArchivingruleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
