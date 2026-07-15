import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { ActivitySuggestionResponse, SuggestionService, TrainConnection } from '@/generated';

import { TrainConnectionResult } from './train-connection-result';

describe('TrainConnectionResult', () => {
  let component: TrainConnectionResult;
  let fixture: ComponentFixture<TrainConnectionResult>;
  let suggestionServiceMock: { suggestActivities: ReturnType<typeof vi.fn> };
  let navigateSpy: ReturnType<typeof vi.spyOn>;

  const connection: TrainConnection = {
    departureTime: '2026-07-01T09:30:00.000Z',
    arrivalTime: '2026-07-01T12:30:00.000Z',
    segments: [
      {
        departureTime: '2026-07-01T09:30:00.000Z',
        arrivalTime: '2026-07-01T11:00:00.000Z',
        start: { id: 'start-1', name: 'Marburg' },
        end: { id: 'mid-1', name: 'Frankfurt (Main) Hbf' },
      },
      {
        departureTime: '2026-07-01T11:15:00.000Z',
        arrivalTime: '2026-07-01T12:30:00.000Z',
        start: { id: 'mid-1', name: 'Frankfurt (Main) Hbf' },
        end: { id: 'end-1', name: 'München Hbf' },
      },
    ],
  };

  const suggestions: ActivitySuggestionResponse = {
    locations: [
      { location: 'Frankfurt (Main) Hbf', activities: ['Skyline ansehen'] },
      { location: 'München Hbf', activities: ['Marienplatz besuchen', 'Englischer Garten'] },
    ],
  };

  beforeEach(async () => {
    suggestionServiceMock = {
      suggestActivities: vi.fn().mockReturnValue(of(suggestions)),
    };

    await TestBed.configureTestingModule({
      imports: [TrainConnectionResult],
      providers: [
        provideRouter([]),
        { provide: SuggestionService, useValue: suggestionServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TrainConnectionResult);
    component = fixture.componentInstance;
    navigateSpy = vi.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch activity suggestions and include them in the description', () => {
    fixture.componentRef.setInput('connection', connection);

    component.goToLogbook();

    expect(suggestionServiceMock.suggestActivities).toHaveBeenCalledTimes(1);
    expect(suggestionServiceMock.suggestActivities).toHaveBeenCalledWith({
      destination: 'München Hbf',
      interchanges: ['Frankfurt (Main) Hbf'],
    });

    expect(navigateSpy).toHaveBeenCalledTimes(1);
    const [route, extras] = navigateSpy.mock.calls[0] as [string[], { queryParams: Record<string, string> }];
    expect(route).toEqual(['/log']);
    expect(extras.queryParams['startStationId']).toBe('start-1');
    expect(extras.queryParams['destinationStationId']).toBe('end-1');
    expect(extras.queryParams['description']).toContain('Übernommener Fahrplan:');
    expect(extras.queryParams['description']).toContain('Vorschläge für unterwegs:');
    expect(extras.queryParams['description']).toContain('- Marienplatz besuchen');
    expect(component.fetchingSuggestions()).toBeFalsy();
  });

  it('should still navigate with the schedule when the suggestion request fails', () => {
    suggestionServiceMock.suggestActivities.mockReturnValue(throwError(() => new Error('unavailable')));
    fixture.componentRef.setInput('connection', connection);

    component.goToLogbook();

    expect(navigateSpy).toHaveBeenCalledTimes(1);
    const [, extras] = navigateSpy.mock.calls[0] as [string[], { queryParams: Record<string, string> }];
    expect(extras.queryParams['description']).toContain('Übernommener Fahrplan:');
    expect(extras.queryParams['description']).not.toContain('Vorschläge für unterwegs:');
    expect(component.fetchingSuggestions()).toBeFalsy();
  });

  it('should not navigate or fetch suggestions without segments', () => {
    component.goToLogbook();

    expect(suggestionServiceMock.suggestActivities).not.toHaveBeenCalled();
    expect(navigateSpy).not.toHaveBeenCalled();
  });
});
