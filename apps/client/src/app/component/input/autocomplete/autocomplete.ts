import {Component, computed, inject, input, output, signal} from '@angular/core';
import {ZardInputDirective} from '@/shared/components/input';
import {HttpClient} from '@angular/common/http';
import {debounceTime, distinctUntilChanged, Observable, Subject, switchMap} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {Station} from '@/generated';
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
  private http = inject(HttpClient);

  placeholder = input<string>('Suche...');
  selectedStation = output<Station>();

  cleared = signal<boolean>(false);

  currentText = "";

  search$ = new Subject<string>();

  results$ = this.search$.pipe(
    debounceTime(300),
    distinctUntilChanged(),
    switchMap(query =>
      this.http.get<Station[]>(`http://localhost:8080/api/v1/route/stations?q=${encodeURIComponent(query)}`)
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
