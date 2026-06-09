import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivruleComponent } from './archivrule.component';

describe('ArchivruleComponent', () => {
  let component: ArchivruleComponent;
  let fixture: ComponentFixture<ArchivruleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArchivruleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArchivruleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
