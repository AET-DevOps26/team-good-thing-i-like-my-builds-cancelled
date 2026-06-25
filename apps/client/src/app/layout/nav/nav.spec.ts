import {ComponentFixture, TestBed} from '@angular/core/testing';

import {Nav} from './nav';
import {provideRouter} from '@angular/router';
import {routes} from '@/app.routes';

describe('Nav', () => {
  let component: Nav;
  let fixture: ComponentFixture<Nav>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Nav],
      providers: [
        provideRouter(routes)
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Nav);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
