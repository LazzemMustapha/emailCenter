import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComptedashboardComponent } from './comptedashboard.component';

describe('ComptedashboardComponent', () => {
  let component: ComptedashboardComponent;
  let fixture: ComponentFixture<ComptedashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ComptedashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ComptedashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
