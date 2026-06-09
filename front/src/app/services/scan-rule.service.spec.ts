import { TestBed } from '@angular/core/testing';

import { ScanRuleService } from './scan-rule.service';

describe('ScanRuleService', () => {
  let service: ScanRuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScanRuleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
