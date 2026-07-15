import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, Subject } from 'rxjs';
import { vi } from 'vitest';
import { LogbookService, TransportMode } from '@/generated';
import { SuggestionEvent, SuggestionService } from '@/shared/services/suggestion.service';

import { Log } from './log';

describe('Log', () => {
  let component: Log;
  let fixture: ComponentFixture<Log>;
  let logbookServiceMock: {
    getLogbookEntries: ReturnType<typeof vi.fn>;
    createLogbookEntry: ReturnType<typeof vi.fn>;
    updateLogbookEntry: ReturnType<typeof vi.fn>;
    deleteLogbookEntry: ReturnType<typeof vi.fn>;
  };
  let suggestionServiceMock: {
    connect: ReturnType<typeof vi.fn>;
    events$: Subject<SuggestionEvent>;
    sendTextUpdate: ReturnType<typeof vi.fn>;
    sendCancel: ReturnType<typeof vi.fn>;
  };
  let activatedRouteMock: {
    snapshot: {
      queryParamMap: ReturnType<typeof convertToParamMap>;
    };
  };

  beforeEach(async () => {
    logbookServiceMock = {
      getLogbookEntries: vi.fn(),
      createLogbookEntry: vi.fn(),
      updateLogbookEntry: vi.fn(),
      deleteLogbookEntry: vi.fn(),
    };
    logbookServiceMock.getLogbookEntries.mockReturnValue(
      of({ items: [], page: 0, size: 8, totalElements: 0 })
    );
    logbookServiceMock.createLogbookEntry.mockReturnValue(
      of({
        id: 'entry-1',
        title: 'Trip',
        startTime: '2026-07-01T09:00:00.000Z',
        endTime: '2026-07-01T10:00:00.000Z',
        startCity: 'Munich',
        destinationCity: 'Berlin',
        transportMode: TransportMode.Train,
        createdAt: '2026-07-01T09:00:00.000Z',
        updatedAt: '2026-07-01T09:00:00.000Z',
      })
    );
    logbookServiceMock.updateLogbookEntry.mockReturnValue(
      of({
        id: 'entry-1',
        title: 'Trip',
        startTime: '2026-07-01T09:00:00.000Z',
        endTime: '2026-07-01T10:00:00.000Z',
        startCity: 'Munich',
        destinationCity: 'Berlin',
        transportMode: TransportMode.Train,
        createdAt: '2026-07-01T09:00:00.000Z',
        updatedAt: '2026-07-01T09:00:00.000Z',
      })
    );
    logbookServiceMock.deleteLogbookEntry.mockReturnValue(of(undefined));

    suggestionServiceMock = {
      connect: vi.fn(),
      events$: new Subject<SuggestionEvent>(),
      sendTextUpdate: vi.fn(),
      sendCancel: vi.fn(),
    };

    activatedRouteMock = {
      snapshot: {
        queryParamMap: convertToParamMap({}),
      },
    };

    await TestBed.configureTestingModule({
      imports: [Log],
      providers: [
        { provide: LogbookService, useValue: logbookServiceMock },
        { provide: SuggestionService, useValue: suggestionServiceMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Log);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should connect to suggestions and load entries on init', () => {
    expect(suggestionServiceMock.connect).toHaveBeenCalledTimes(1);
    expect(logbookServiceMock.getLogbookEntries).toHaveBeenCalledTimes(1);
  });

  it('should not save entry when required fields are missing', () => {
    logbookServiceMock.createLogbookEntry.mockClear();
    component.title.set('');
    component.startCity.set('Munich');
    component.destinationCity.set('Berlin');

    component.saveEntry();

    expect(logbookServiceMock.createLogbookEntry).not.toHaveBeenCalled();
    expect(logbookServiceMock.updateLogbookEntry).not.toHaveBeenCalled();
  });

  it('should create a new entry with trimmed payload and reload list', () => {
    logbookServiceMock.getLogbookEntries.mockClear();

    component.title.set('  Morning commute  ');
    component.reportText.set('  Some details  ');
    component.startCity.set('  Munich  ');
    component.destinationCity.set('  Berlin  ');
    component.startStationId.set(' 8000261 ');
    component.destinationStationId.set(' 8011160 ');
    component.selectedTransportMode.set(TransportMode.Train);
    component.startTime.set('2026-07-01T09:00');
    component.endTime.set('2026-07-01T10:00');

    component.saveEntry();

    expect(logbookServiceMock.createLogbookEntry).toHaveBeenCalledTimes(1);
    const payload = logbookServiceMock.createLogbookEntry.mock.calls[0][0];
    expect(payload.title).toBe('Morning commute');
    expect(payload.description).toBe('Some details');
    expect(payload.startCity).toBe('Munich');
    expect(payload.destinationCity).toBe('Berlin');
    expect(payload.startStationId).toBe('8000261');
    expect(payload.destinationStationId).toBe('8011160');
    expect(logbookServiceMock.getLogbookEntries).toHaveBeenCalledTimes(1);
    expect(component.title()).toBe('');
    expect(component.page()).toBe(0);
  });

  it('should debounce text updates before asking for a suggestion', () => {
    vi.useFakeTimers();

    const event = {
      target: {
        value: 'The train was delayed',
        selectionStart: 21,
      },
    } as unknown as Event;

    component.onInput(event);
    vi.advanceTimersByTime(599);
    expect(suggestionServiceMock.sendTextUpdate).not.toHaveBeenCalled();

    vi.advanceTimersByTime(1);
    expect(suggestionServiceMock.sendTextUpdate).toHaveBeenCalledWith('The train was delayed', '');
    expect(component.isStreaming()).toBeTruthy();

    vi.useRealTimers();
  });

  it('should prefill form values from query params', async () => {
    activatedRouteMock.snapshot.queryParamMap = convertToParamMap({
      startStationId: '8000105',
      startStationName: 'Frankfurt(Main)Hbf',
      destinationStationId: '8000261',
      destinationStationName: 'Muenchen Hbf',
      startTime: '2026-07-01T09:00:00.000Z',
      endTime: '2026-07-01T12:00:00.000Z',
      description: 'Uebernommener Fahrplan:\n09:00 - 12:00',
    });

    fixture = TestBed.createComponent(Log);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.startStationId()).toBe('8000105');
    expect(component.destinationStationId()).toBe('8000261');
    expect(component.startCity()).toBe('Frankfurt(Main)Hbf');
    expect(component.destinationCity()).toBe('Muenchen Hbf');
    expect(component.title()).toBe('Von Frankfurt(Main)Hbf nach Muenchen Hbf');
    expect(component.reportText()).toContain('Uebernommener Fahrplan');
  });
});
