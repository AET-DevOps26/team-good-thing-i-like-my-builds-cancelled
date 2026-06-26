import {Component, inject, input, output, signal} from '@angular/core';
import {ZardInputDirective} from '@/shared/components/input';
import {debounceTime, distinctUntilChanged, Subject, switchMap} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {RouteService, Station} from '@/generated';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-autocomplete',
  imports: [
    ZardInputDirective,
    AsyncPipe,
    FormsModule
  ],
  templateUrl: './autocomplete.html',
  styleUrl: './autocomplete.scss',
})
export class Autocomplete {
  private routeService = inject(RouteService);

  placeholder = input<string>('Suche...');
  selectedStation = output<Station>();

  cleared = signal<boolean>(false);

  currentText = "";

  search$ = new Subject<string>();

  results$ = this.search$.pipe(
    debounceTime(300),
    distinctUntilChanged(),
    switchMap(query =>
      this.routeService.getStations(query)
    )
  );

  onInput(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.cleared.set(false);
    console.log('Input value:', value);
    this.search$.next(value);
  }

  select(value: Station) {
    console.log('Selected:', value);
    this.search$.next("")
    this.cleared.set(true);
    this.currentText = value.name ?? "-";
    this.selectedStation.emit(value)
  }
}
