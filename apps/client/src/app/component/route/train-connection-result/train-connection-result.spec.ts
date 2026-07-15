import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Router, provideRouter } from '@angular/router';
import { NEVER, Subject, of, throwError } from 'rxjs';
import { vi } from 'vitest';
import { ActivitySuggestionResponse, SuggestionService, TrainConnection } from '@/generated';
import { ZardAccordionItemComponent } from '@/shared/components/accordion';

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
    fixture.detectChanges();
    await fixture.whenStable();
  });

  function openAccordion(): void {
    const accordionItem = fixture.debugElement
      .query(By.directive(ZardAccordionItemComponent))
      .componentInstance as ZardAccordionItemComponent;
    accordionItem.isOpen.set(true);
    fixture.detectChanges();
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch suggestions when the accordion item is opened and show them read-only', () => {
    fixture.componentRef.setInput('connection', connection);
    openAccordion();

    expect(suggestionServiceMock.suggestActivities).toHaveBeenCalledTimes(1);
    expect(suggestionServiceMock.suggestActivities).toHaveBeenCalledWith({
      destination: 'München Hbf',
      interchanges: ['Frankfurt (Main) Hbf'],
    });

    fixture.detectChanges();
    const textarea = fixture.nativeElement.querySelector('textarea') as HTMLTextAreaElement;
    expect(textarea).toBeTruthy();
    expect(textarea.readOnly).toBeTruthy();
    expect(textarea.value).toContain('Vorschläge für unterwegs:');
    expect(textarea.value).toContain('- Marienplatz besuchen');
    expect(component.fetchingSuggestions()).toBeFalsy();
  });

  it('should not fetch again for repeated opens or logbook clicks', () => {
    fixture.componentRef.setInput('connection', connection);
    openAccordion();
    component.goToLogbook();

    expect(suggestionServiceMock.suggestActivities).toHaveBeenCalledTimes(1);
  });

  it('should include the fetched suggestions when navigating to the logbook', () => {
    fixture.componentRef.setInput('connection', connection);
    openAccordion();

    component.goToLogbook();

    expect(navigateSpy).toHaveBeenCalledTimes(1);
    const [route, extras] = navigateSpy.mock.calls[0] as [string[], { queryParams: Record<string, string> }];
    expect(route).toEqual(['/log']);
    expect(extras.queryParams['startStationId']).toBe('start-1');
    expect(extras.queryParams['destinationStationId']).toBe('end-1');
    expect(extras.queryParams['description']).toContain('Übernommener Fahrplan:');
    expect(extras.queryParams['description']).toContain('Vorschläge für unterwegs:');
    expect(extras.queryParams['description']).toContain('- Marienplatz besuchen');
  });

  it('should wait for a running request before navigating to the logbook', () => {
    const response$ = new Subject<ActivitySuggestionResponse>();
    suggestionServiceMock.suggestActivities.mockReturnValue(response$.asObservable());
    fixture.componentRef.setInput('connection', connection);
    openAccordion();

    component.goToLogbook();
    expect(navigateSpy).not.toHaveBeenCalled();
    expect(component.pendingNavigation()).toBeTruthy();

    response$.next(suggestions);
    response$.complete();

    expect(navigateSpy).toHaveBeenCalledTimes(1);
    const [, extras] = navigateSpy.mock.calls[0] as [string[], { queryParams: Record<string, string> }];
    expect(extras.queryParams['description']).toContain('Vorschläge für unterwegs:');
    expect(component.pendingNavigation()).toBeFalsy();
    expect(component.fetchingSuggestions()).toBeFalsy();
  });

  it('should still navigate with the schedule when the suggestion request fails', () => {
    suggestionServiceMock.suggestActivities.mockReturnValue(throwError(() => new Error('unavailable')));
    fixture.componentRef.setInput('connection', connection);
    openAccordion();

    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('textarea')).toBeNull();

    component.goToLogbook();

    expect(navigateSpy).toHaveBeenCalledTimes(1);
    const [, extras] = navigateSpy.mock.calls[0] as [string[], { queryParams: Record<string, string> }];
    expect(extras.queryParams['description']).toContain('Übernommener Fahrplan:');
    expect(extras.queryParams['description']).not.toContain('Vorschläge für unterwegs:');
  });

  it('should navigate without suggestions when the request times out', () => {
    vi.useFakeTimers();
    try {
      suggestionServiceMock.suggestActivities.mockReturnValue(NEVER);
      fixture.componentRef.setInput('connection', connection);
      openAccordion();

      component.goToLogbook();
      expect(navigateSpy).not.toHaveBeenCalled();

      vi.advanceTimersByTime(10_001);

      expect(navigateSpy).toHaveBeenCalledTimes(1);
      const [, extras] = navigateSpy.mock.calls[0] as [string[], { queryParams: Record<string, string> }];
      expect(extras.queryParams['description']).toContain('Übernommener Fahrplan:');
      expect(extras.queryParams['description']).not.toContain('Vorschläge für unterwegs:');
      expect(component.pendingNavigation()).toBeFalsy();
      expect(component.fetchingSuggestions()).toBeFalsy();
    } finally {
      vi.useRealTimers();
    }
  });

  it('should not navigate or fetch suggestions without segments', () => {
    openAccordion();
    component.goToLogbook();

    expect(suggestionServiceMock.suggestActivities).not.toHaveBeenCalled();
    expect(navigateSpy).not.toHaveBeenCalled();
  });
});
