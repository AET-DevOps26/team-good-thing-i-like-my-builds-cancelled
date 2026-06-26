import {Component, inject, model, OnInit, signal} from '@angular/core';
import {ZardInputDirective} from '@/shared/components/input';
import {ZardDatePickerComponent} from '@/shared/components/date-picker';
import {Autocomplete} from '@/component/input/autocomplete/autocomplete';
import {RouteService, Station, TrainConnection} from '@/generated';
import {ZardButtonComponent} from '@/shared/components/button';
import {HttpClient} from '@angular/common/http';
import {TrainConnectionResult} from '@/component/route/train-connection-result/train-connection-result';
import {ZardAccordionComponent} from '@/shared/components/accordion';
import {ZardLoaderComponent} from '@/shared/components/loader';

@Component({
  selector: 'app-plan',
  imports: [
    ZardInputDirective,
    ZardDatePickerComponent,
    Autocomplete,
    ZardButtonComponent,
    TrainConnectionResult,
    ZardAccordionComponent,
    ZardLoaderComponent
  ],
  templateUrl: './plan.html',
  styleUrl: './plan.scss',
})
export class Plan implements OnInit {
  private http = inject(HttpClient);
  private routeService = inject(RouteService);

  selectedStartStation: Station | null = null;
  selectedEndStation: Station | null = null;
  readonly selectedDate = signal<Date | null>(null);
  selectedTime = model<string | null>(null);

  connections = signal<TrainConnection[]>([])

  searched = signal<boolean>(false);
  fetching = signal<boolean>(false);

  ngOnInit() {
    const now = new Date();
    this.selectedDate.set(now);
    this.selectedTime.set(now.getHours() + ":" + now.getMinutes());
  }

  onDateChange(date: Date | null) {
    this.searched.set(false);
    this.selectedDate.set(date);
    console.log('Selected date:', date);
  }

  protected fetchRoutes() {
    if (this.selectedStartStation && this.selectedEndStation && this.selectedDate() && this.selectedTime()) {
      this.fetching.set(true)
      this.searched.set(true);
      console.log('Fetching routes from', this.selectedStartStation.name, 'to', this.selectedEndStation.name, 'on', this.selectedDate(), 'at', this.selectedTime());

      const [h, m] = this.selectedTime()!.split(':')
      const time = this.selectedDate()!;
      time.setHours(+h, +m, 0, 0);

      this.routeService.getConnections(time.toISOString(), this.selectedStartStation.id!, this.selectedEndStation.id!).subscribe({
        next: value => {
          this.connections.set(value);
        }, error: error => {
          console.log(error);
        }, complete: () => {
          this.fetching.set(false);
        }
      })
    } else {
      console.log('Please select both stations and a date before fetching routes.');
    }
  }
}
