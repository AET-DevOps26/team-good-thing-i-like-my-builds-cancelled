import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoContent } from './no-content';

describe('NoContent', () => {
  let component: NoContent;
  let fixture: ComponentFixture<NoContent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NoContent],
    }).compileComponents();

    fixture = TestBed.createComponent(NoContent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
