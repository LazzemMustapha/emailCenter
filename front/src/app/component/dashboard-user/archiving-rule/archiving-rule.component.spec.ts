import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivingRuleComponent } from './archiving-rule.component';

describe('ArchivingRuleComponent', () => {
  let component: ArchivingRuleComponent;
  let fixture: ComponentFixture<ArchivingRuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArchivingRuleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArchivingRuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
