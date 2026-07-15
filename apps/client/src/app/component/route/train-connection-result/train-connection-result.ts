import {Component, computed, DestroyRef, effect, inject, input, signal, viewChild} from '@angular/core';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {Router} from '@angular/router';
import {timeout} from 'rxjs';
import {ActivitySuggestionResponse, SuggestionService, TrainConnection} from '@/generated';
import {ZardAccordionItemComponent} from '@/shared/components/accordion';
import {ZardButtonComponent} from '@/shared/components/button';
import {ZardInputDirective} from '@/shared/components/input';
import {ZardLoaderComponent} from '@/shared/components/loader';

@Component({
  selector: 'app-train-connection-result',
  imports: [
    ZardAccordionItemComponent,
    ZardButtonComponent,
    ZardInputDirective,
    ZardLoaderComponent
  ],
  templateUrl: './train-connection-result.html',
  styleUrl: './train-connection-result.scss',
})
export class TrainConnectionResult {
  private static readonly SUGGESTION_TIMEOUT_MS = 10_000;

  private router = inject(Router);
  private suggestionService = inject(SuggestionService);
  private destroyRef = inject(DestroyRef);

  private accordionItem = viewChild(ZardAccordionItemComponent);

  connection = input<TrainConnection>(
    {departureTime: "2026-06-25T13:34:00+02:00", arrivalTime: "2026-06-25T14:15:00+02:00", segments: []} as TrainConnection);

  fetchingSuggestions = signal<boolean>(false);
  /** null = not loaded yet, '' = loaded without result (error/timeout/no destination). */
  activitySuggestions = signal<string | null>(null);
  pendingNavigation = signal<boolean>(false);

  private suggestionsRequested = false;

  constructor() {
    effect(() => {
      if (this.accordionItem()?.isOpen() && this.connection().segments.length > 0) {
        this.fetchSuggestions();
      }
    });
  }

  startAt = computed(() => {
    return this.getTimeString(this.connection().departureTime);
  });

  endAt = computed(() => {
    return this.getTimeString(this.connection().arrivalTime);
  });

  getTimeString(dateString: string): string {
    const date = new Date(dateString);
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
  }

  goToLogbook(): void {
    const segments = this.connection().segments;
    if (segments.length === 0 || this.pendingNavigation()) {
      return;
    }

    this.fetchSuggestions();

    if (this.activitySuggestions() === null) {
      this.pendingNavigation.set(true);
      return;
    }

    this.navigateToLogbook();
  }

  private fetchSuggestions(): void {
    if (this.suggestionsRequested) {
      return;
    }

    const segments = this.connection().segments;
    const destination = segments[segments.length - 1].end.name ?? '';
    if (destination === '') {
      this.suggestionsRequested = true;
      this.activitySuggestions.set('');
      return;
    }

    const interchanges = segments.slice(0, -1)
      .map(segment => segment.end.name ?? '')
      .filter(name => name !== '');

    this.suggestionsRequested = true;
    this.fetchingSuggestions.set(true);
    this.suggestionService.suggestActivities({destination, interchanges})
      .pipe(
        timeout({first: TrainConnectionResult.SUGGESTION_TIMEOUT_MS}),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: response => {
          this.activitySuggestions.set(this.buildActivityDescription(response));
        },
        error: error => {
          console.error(error);
          this.activitySuggestions.set('');
          this.fetchingSuggestions.set(false);
          this.finishPendingNavigation();
        },
        complete: () => {
          this.fetchingSuggestions.set(false);
          this.finishPendingNavigation();
        },
      });
  }

  private finishPendingNavigation(): void {
    if (this.pendingNavigation()) {
      this.pendingNavigation.set(false);
      this.navigateToLogbook();
    }
  }

  private navigateToLogbook(): void {
    const segments = this.connection().segments;
    const firstSegment = segments[0];
    const lastSegment = segments[segments.length - 1];

    const description = [this.buildScheduleDescription(), this.activitySuggestions() ?? '']
      .filter(part => part !== '')
      .join('\n\n');

    this.router.navigate(['/log'], {
      queryParams: {
        startStationId: firstSegment.start.id,
        startStationName: firstSegment.start.name ?? '',
        destinationStationId: lastSegment.end.id,
        destinationStationName: lastSegment.end.name ?? '',
        startTime: this.connection().departureTime,
        endTime: this.connection().arrivalTime,
        description,
      },
    });
  }

  private buildScheduleDescription(): string {
    const lines = this.connection().segments.map((segment, index) => {
      const legIndex = index + 1;
      return `${legIndex}. ${segment.start.name ?? 'Unbekannt'} (${this.getTimeString(segment.departureTime)}) -> ${segment.end.name ?? 'Unbekannt'} (${this.getTimeString(segment.arrivalTime)})`;
    });

    return [
      'Übernommener Fahrplan:',
      `${this.getTimeString(this.connection().departureTime)} - ${this.getTimeString(this.connection().arrivalTime)}`,
      ...lines,
    ].join('\n');
  }

  private buildActivityDescription(response: ActivitySuggestionResponse): string {
    if (response.locations.length === 0) {
      return '';
    }

    const sections = response.locations.map(location =>
      [`${location.location}:`, ...location.activities.map(activity => `- ${activity}`)].join('\n'));

    return ['Vorschläge für unterwegs:', ...sections].join('\n');
  }

}
