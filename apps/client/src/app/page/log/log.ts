import {
  Component,
  computed,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { ZardButtonComponent } from '@/shared/components/button';
import { ZardInputDirective } from '@/shared/components/input';
import { ZardSelectComponent, ZardSelectItemComponent } from '@/shared/components/select';
import { ZardPaginationComponent } from '@/shared/components/pagination';
import { Autocomplete } from '@/component/input/autocomplete/autocomplete';
import {
  CreateLogbookEntryRequest,
  LogbookEntry,
  LogbookService,
  Station,
  TransportMode,
  UpdateLogbookEntryRequest,
} from '@/generated';
import { SuggestionService } from '@/shared/services/suggestion.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-log',
  imports: [
    ZardButtonComponent,
    ZardInputDirective,
    ZardSelectComponent,
    ZardSelectItemComponent,
    ZardPaginationComponent,
    Autocomplete,
    DatePipe,
  ],
  templateUrl: './log.html',
  styleUrl: './log.scss',
})
export class Log implements OnInit, OnDestroy {
  @ViewChild('editor') editorRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('overlay') overlayRef!: ElementRef<HTMLDivElement>;

  private suggestion = inject(SuggestionService);
  private logbookService = inject(LogbookService);
  private route = inject(ActivatedRoute);
  private sub!: Subscription;
  private debounceTimer: ReturnType<typeof setTimeout> | null = null;
  private searchTimer: ReturnType<typeof setTimeout> | null = null;

  readonly reportText = signal('');
  readonly ghostText = signal('');
  // Cursor position the current ghost suggestion is anchored to; null = end of text
  readonly ghostPosition = signal<number | null>(null);
  readonly textBeforeGhost = computed(() => {
    const pos = this.ghostPosition();
    const text = this.reportText();
    return pos === null ? text : text.slice(0, pos);
  });
  readonly textAfterGhost = computed(() => {
    const pos = this.ghostPosition();
    return pos === null ? '' : this.reportText().slice(pos);
  });
  readonly isStreaming = signal(false);
  readonly isLoading = signal(false);
  readonly isSaving = signal(false);

  readonly entries = signal<LogbookEntry[]>([]);
  readonly searchTerm = signal('');
  readonly selectedTransportFilter = signal<TransportMode | null>(null);
  readonly page = signal(0);
  readonly size = signal(8);
  readonly totalElements = signal(0);

  readonly editingEntryId = signal<string | null>(null);
  readonly title = signal('');
  readonly startTime = signal(this.toLocalDateTimeInput(new Date()));
  readonly endTime = signal(this.toLocalDateTimeInput(new Date(Date.now() + 60 * 60 * 1000)));
  readonly startCity = signal('');
  readonly destinationCity = signal('');
  readonly startStationId = signal('');
  readonly destinationStationId = signal('');
  readonly selectedTransportMode = signal<TransportMode>(TransportMode.Train);
  readonly calendarDate = signal<Date | null>(new Date());

  readonly transportOptions: { label: string; value: TransportMode }[] = [
    { label: 'Zug', value: TransportMode.Train },
    { label: 'Bus', value: TransportMode.Bus },
    { label: 'Tram', value: TransportMode.Tram },
    { label: 'U-Bahn', value: TransportMode.Subway },
    { label: 'Fähre', value: TransportMode.Ferry },
    { label: 'Flugzeug', value: TransportMode.Flight },
    { label: 'Auto', value: TransportMode.Car },
    { label: 'Fahrrad', value: TransportMode.Bike },
    { label: 'Zu Fuß', value: TransportMode.Walk },
    { label: 'Sonstiges', value: TransportMode.Other },
  ];

  ngOnInit(): void {
    this.applyPrefillFromQueryParams();
    this.suggestion.connect();
    this.loadEntries();
    this.sub = this.suggestion.events$.subscribe((event) => {
      if (event.type === 'token') {
        this.ghostText.update((g) => g + event.token);
      } else if (event.type === 'done') {
        this.isStreaming.set(false);
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    if (this.debounceTimer) clearTimeout(this.debounceTimer);
    if (this.searchTimer) clearTimeout(this.searchTimer);
  }

  onScroll(event: Event): void {
    const overlay = this.overlayRef?.nativeElement;
    if (overlay) overlay.scrollTop = (event.target as HTMLTextAreaElement).scrollTop;
  }

  onInput(event: Event): void {
    const el = event.target as HTMLTextAreaElement;
    const text = el.value;
    this.reportText.set(text);

    this.ghostText.set('');
    this.ghostPosition.set(null);
    this.isStreaming.set(false);
    if (this.debounceTimer) clearTimeout(this.debounceTimer);

    if (!text.trim()) return;

    this.debounceTimer = setTimeout(() => {
      const cursor = el.selectionStart ?? text.length;
      const textBefore = text.slice(0, cursor);
      const textAfter = text.slice(cursor);

      this.ghostPosition.set(cursor);
      this.isStreaming.set(true);
      this.suggestion.sendTextUpdate(textBefore, textAfter);
    }, 600);
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Tab' && this.ghostText()) {
      event.preventDefault();
      this.acceptSuggestion();
      return;
    }
    if (event.key === 'Escape' && this.ghostText()) {
      this.discardSuggestion();
      return;
    }
  }

  acceptSuggestion(): void {
    const ghost = this.ghostText();
    if (!ghost) return;
    const el = this.editorRef?.nativeElement;
    const cursor = this.ghostPosition() ?? el?.selectionStart ?? this.reportText().length;
    const before = this.reportText().slice(0, cursor);
    const after = this.reportText().slice(cursor);
    this.reportText.set(before + ghost + after);
    this.ghostText.set('');
    this.ghostPosition.set(null);
    this.isStreaming.set(false);
    this.suggestion.sendCancel();
    // Move cursor to end of inserted text
    const newCursor = cursor + ghost.length;
    setTimeout(() => {
      el?.setSelectionRange(newCursor, newCursor);
      el?.focus();
    });
  }

  discardSuggestion(): void {
    this.ghostText.set('');
    this.ghostPosition.set(null);
    this.isStreaming.set(false);
    this.suggestion.sendCancel();
  }

  clearReport(): void {
    this.reportText.set('');
    this.ghostText.set('');
    this.ghostPosition.set(null);
    this.isStreaming.set(false);
    this.suggestion.sendCancel();
  }

  onSearchInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm.set(value);
    if (this.searchTimer) clearTimeout(this.searchTimer);
    this.searchTimer = setTimeout(() => {
      this.page.set(0);
      this.loadEntries();
    }, 300);
  }

  onFilterTransportChange(value: string | string[]): void {
    const selected = Array.isArray(value) ? value[0] : value;
    this.selectedTransportFilter.set(selected ? (selected as TransportMode) : null);
    this.page.set(0);
    this.loadEntries();
  }

  onTransportModeChange(value: string | string[]): void {
    const selected = Array.isArray(value) ? value[0] : value;
    if (selected) {
      this.selectedTransportMode.set(selected as TransportMode);
    }
  }

  onCalendarDateChange(date: Date | Date[]): void {
    const selected = Array.isArray(date) ? date[0] : date;
    if (!selected) {
      return;
    }
    this.calendarDate.set(selected);
    const start = new Date(this.startTime());
    start.setFullYear(selected.getFullYear(), selected.getMonth(), selected.getDate());
    const end = new Date(this.endTime());
    end.setFullYear(selected.getFullYear(), selected.getMonth(), selected.getDate());
    this.startTime.set(this.toLocalDateTimeInput(start));
    this.endTime.set(this.toLocalDateTimeInput(end));
  }

  onStartStationSelected(station: Station): void {
    this.startStationId.set(station.id ?? '');
    if (station.name?.trim()) {
      this.startCity.set(station.name.trim());
    }
  }

  onDestinationStationSelected(station: Station): void {
    this.destinationStationId.set(station.id ?? '');
    if (station.name?.trim()) {
      this.destinationCity.set(station.name.trim());
    }
  }

  clearStartStationSelection(): void {
    this.startStationId.set('');
  }

  clearDestinationStationSelection(): void {
    this.destinationStationId.set('');
  }

  goToPreviousPage(): void {
    if (this.page() <= 0) {
      return;
    }
    this.page.update((p) => p - 1);
    this.loadEntries();
  }

  goToNextPage(): void {
    if ((this.page() + 1) * this.size() >= this.totalElements()) {
      return;
    }
    this.page.update((p) => p + 1);
    this.loadEntries();
  }

  onPaginationChange(nextPageIndexOneBased: number): void {
    const nextZeroBased = Math.max(0, nextPageIndexOneBased - 1);
    if (nextZeroBased === this.page()) {
      return;
    }
    this.page.set(nextZeroBased);
    this.loadEntries();
  }

  totalPages(): number {
    return Math.max(1, Math.ceil(this.totalElements() / this.size()));
  }

  beginCreate(): void {
    this.editingEntryId.set(null);
    this.title.set('');
    this.reportText.set('');
    this.ghostText.set('');
    this.ghostPosition.set(null);
    const now = new Date();
    this.startTime.set(this.toLocalDateTimeInput(now));
    this.endTime.set(this.toLocalDateTimeInput(new Date(now.getTime() + 60 * 60 * 1000)));
    this.startCity.set('');
    this.destinationCity.set('');
    this.startStationId.set('');
    this.destinationStationId.set('');
    this.selectedTransportMode.set(TransportMode.Train);
    this.calendarDate.set(now);
  }

  beginEdit(entry: LogbookEntry): void {
    this.editingEntryId.set(entry.id);
    this.title.set(entry.title);
    this.reportText.set(entry.description ?? '');
    this.ghostText.set('');
    this.ghostPosition.set(null);
    this.startTime.set(this.toLocalDateTimeInput(new Date(entry.startTime)));
    this.endTime.set(this.toLocalDateTimeInput(new Date(entry.endTime)));
    this.startCity.set(entry.startCity);
    this.destinationCity.set(entry.destinationCity);
    this.startStationId.set(entry.startStationId ?? '');
    this.destinationStationId.set(entry.destinationStationId ?? '');
    this.selectedTransportMode.set(entry.transportMode);
    this.calendarDate.set(new Date(entry.startTime));
  }

  deleteEntry(entry: LogbookEntry): void {
    if (!confirm(`Eintrag "${entry.title}" wirklich loeschen?`)) {
      return;
    }
    this.logbookService.deleteLogbookEntry(entry.id).subscribe({
      next: () => {
        if (this.editingEntryId() === entry.id) {
          this.beginCreate();
        }
        this.loadEntries();
      },
      error: (err) => {
        console.error('Delete failed', err);
      },
    });
  }

  saveEntry(): void {
    if (!this.title().trim() || !this.startCity().trim() || !this.destinationCity().trim()) {
      return;
    }

    const payload: CreateLogbookEntryRequest = {
      title: this.title().trim(),
      description: this.reportText().trim() || undefined,
      startTime: new Date(this.startTime()).toISOString(),
      endTime: new Date(this.endTime()).toISOString(),
      startCity: this.startCity().trim(),
      destinationCity: this.destinationCity().trim(),
      startStationId: this.startStationId().trim() || undefined,
      destinationStationId: this.destinationStationId().trim() || undefined,
      transportMode: this.selectedTransportMode(),
    };

    this.isSaving.set(true);

    const editingId = this.editingEntryId();
    if (editingId) {
      this.logbookService.updateLogbookEntry(editingId, payload as UpdateLogbookEntryRequest).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.loadEntries();
        },
        error: (err) => {
          this.isSaving.set(false);
          console.error('Update failed', err);
        },
      });
      return;
    }

    this.logbookService.createLogbookEntry(payload).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.beginCreate();
        this.page.set(0);
        this.loadEntries();
      },
      error: (err) => {
        this.isSaving.set(false);
        console.error('Create failed', err);
      },
    });
  }

  cancelEdit(): void {
    this.beginCreate();
  }

  trackByEntryId(_: number, entry: LogbookEntry): string {
    return entry.id;
  }

  protected entryPageLabel(): string {
    if (this.totalElements() === 0) {
      return '0 von 0';
    }
    const start = this.page() * this.size() + 1;
    const end = Math.min((this.page() + 1) * this.size(), this.totalElements());
    return `${start}-${end} von ${this.totalElements()}`;
  }

  private loadEntries(): void {
    this.isLoading.set(true);
    const query = this.searchTerm().trim() || undefined;
    const mode = this.selectedTransportFilter() ?? undefined;
    this.logbookService
      .getLogbookEntries(undefined, undefined, query, mode, this.page(), this.size())
      .subscribe({
        next: (result) => {
          this.entries.set(result.items ?? []);
          this.totalElements.set(result.totalElements ?? 0);
          this.isLoading.set(false);
        },
        error: (err) => {
          this.isLoading.set(false);
          console.error('Load entries failed', err);
        },
      });
  }

  private toLocalDateTimeInput(date: Date): string {
    const offset = date.getTimezoneOffset();
    const local = new Date(date.getTime() - offset * 60 * 1000);
    return local.toISOString().slice(0, 16);
  }

  private applyPrefillFromQueryParams(): void {
    const params = this.route.snapshot.queryParamMap;
    const startStationId = params.get('startStationId') ?? '';
    const startStationName = params.get('startStationName') ?? '';
    const destinationStationId = params.get('destinationStationId') ?? '';
    const destinationStationName = params.get('destinationStationName') ?? '';
    const startTime = params.get('startTime');
    const endTime = params.get('endTime');
    const description = params.get('description') ?? '';

    if (!startStationId && !startStationName && !destinationStationId && !destinationStationName && !startTime && !endTime && !description) {
      return;
    }

    this.editingEntryId.set(null);
    this.ghostText.set('');
    this.ghostPosition.set(null);
    this.isStreaming.set(false);
    this.selectedTransportMode.set(TransportMode.Train);

    this.startStationId.set(startStationId);
    this.destinationStationId.set(destinationStationId);
    this.startCity.set(startStationName);
    this.destinationCity.set(destinationStationName);
    this.title.set(this.buildPrefillTitle(startStationName, destinationStationName));
    this.reportText.set(description);

    if (startTime) {
      const startDate = new Date(startTime);
      if (!Number.isNaN(startDate.getTime())) {
        this.startTime.set(this.toLocalDateTimeInput(startDate));
        this.calendarDate.set(startDate);
      }
    }

    if (endTime) {
      const endDate = new Date(endTime);
      if (!Number.isNaN(endDate.getTime())) {
        this.endTime.set(this.toLocalDateTimeInput(endDate));
      }
    }
  }

  private buildPrefillTitle(startStationName: string, destinationStationName: string): string {
    if (!startStationName && !destinationStationName) {
      return 'Reiseeintrag';
    }
    if (!startStationName) {
      return `Anreise nach ${destinationStationName}`;
    }
    if (!destinationStationName) {
      return `Abfahrt von ${startStationName}`;
    }
    return `Von ${startStationName} nach ${destinationStationName}`;
  }
}
