import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Plan } from './plan';

describe('Plan', () => {
  let component: Plan;
  let fixture: ComponentFixture<Plan>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Plan],
    }).compileComponents();

    fixture = TestBed.createComponent(Plan);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
