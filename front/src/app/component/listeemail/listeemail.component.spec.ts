import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeemailComponent } from './listeemail.component';

describe('ListeemailComponent', () => {
  let component: ListeemailComponent;
  let fixture: ComponentFixture<ListeemailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListeemailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListeemailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
