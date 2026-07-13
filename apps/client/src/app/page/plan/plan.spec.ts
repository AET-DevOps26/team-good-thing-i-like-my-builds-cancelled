import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { vi } from 'vitest';
import { RouteService, Station, TrainConnection } from '@/generated';

import { Plan } from './plan';

type PlanTestAccess = Plan & {
  fetchRoutes: () => void;
};

describe('Plan', () => {
  let component: Plan;
  let fixture: ComponentFixture<Plan>;
  let routeServiceMock: { getConnections: ReturnType<typeof vi.fn> };

  const connections: TrainConnection[] = [
    {
      departureTime: '2026-07-01T09:30:00.000Z',
      arrivalTime: '2026-07-01T10:30:00.000Z',
      segments: [],
    },
  ];

  beforeEach(async () => {
    routeServiceMock = {
      getConnections: vi.fn().mockReturnValue(of(connections)),
    };

    await TestBed.configureTestingModule({
      imports: [Plan],
      providers: [{ provide: RouteService, useValue: routeServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(Plan);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize date and time on init', () => {
    expect(component.selectedDate()).not.toBeNull();
    expect(component.selectedTime()).not.toBeNull();
    expect(component.selectedTime()).toContain(':');
  });

  it('should fetch routes and update state when required data is present', () => {
    const start: Station = { id: 'start-1', name: 'Start' };
    const end: Station = { id: 'end-1', name: 'End' };
    const selectedDate = new Date('2026-07-01T00:00:00.000Z');
    const expected = new Date(selectedDate);
    expected.setHours(9, 30, 0, 0);

    component.selectedStartStation = start;
    component.selectedEndStation = end;
    component.selectedDate.set(selectedDate);
    component.selectedTime.set('09:30');

    (component as PlanTestAccess).fetchRoutes();

    expect(routeServiceMock.getConnections).toHaveBeenCalledTimes(1);
    expect(routeServiceMock.getConnections).toHaveBeenCalledWith(
      expected.toISOString(),
      'start-1',
      'end-1'
    );
    expect(component.searched()).toBeTruthy();
    expect(component.fetching()).toBeFalsy();
    expect(component.connections()).toEqual(connections);
  });

  it('should not fetch routes when form is incomplete', () => {
    component.selectedStartStation = { id: 'start-1' };
    component.selectedEndStation = null;
    component.selectedDate.set(new Date());
    component.selectedTime.set('09:30');

    (component as PlanTestAccess).fetchRoutes();

    expect(routeServiceMock.getConnections).not.toHaveBeenCalled();
    expect(component.searched()).toBeFalsy();
    expect(component.fetching()).toBeFalsy();
  });
});
