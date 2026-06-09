import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScanEmailComponent } from './scan-email.component';

describe('ScanEmailComponent', () => {
  let component: ScanEmailComponent;
  let fixture: ComponentFixture<ScanEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScanEmailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ScanEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
