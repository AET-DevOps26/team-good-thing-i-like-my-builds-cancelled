import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainConnectionResult } from './train-connection-result';

describe('TrainConnectionResult', () => {
  let component: TrainConnectionResult;
  let fixture: ComponentFixture<TrainConnectionResult>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrainConnectionResult],
    }).compileComponents();

    fixture = TestBed.createComponent(TrainConnectionResult);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
