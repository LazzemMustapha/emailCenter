import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivedEmailComponent } from './archived-email.component';

describe('ArchivedEmailComponent', () => {
  let component: ArchivedEmailComponent;
  let fixture: ComponentFixture<ArchivedEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArchivedEmailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArchivedEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
