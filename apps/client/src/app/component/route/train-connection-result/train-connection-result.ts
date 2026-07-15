import {Component, computed, inject, input} from '@angular/core';
import {Router} from '@angular/router';
import {TrainConnection} from '@/generated';
import {ZardAccordionItemComponent} from '@/shared/components/accordion';
import {ZardButtonComponent} from '@/shared/components/button';

@Component({
  selector: 'app-train-connection-result',
  imports: [
    ZardAccordionItemComponent,
    ZardButtonComponent
  ],
  templateUrl: './train-connection-result.html',
  styleUrl: './train-connection-result.scss',
})
export class TrainConnectionResult {
  private router = inject(Router);

  connection = input<TrainConnection>(
    {departureTime: "2026-06-25T13:34:00+02:00", arrivalTime: "2026-06-25T14:15:00+02:00", segments: []} as TrainConnection);

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
    if (segments.length === 0) {
      return;
    }

    const firstSegment = segments[0];
    const lastSegment = segments[segments.length - 1];

    this.router.navigate(['/log'], {
      queryParams: {
        startStationId: firstSegment.start.id,
        startStationName: firstSegment.start.name ?? '',
        destinationStationId: lastSegment.end.id,
        destinationStationName: lastSegment.end.name ?? '',
        startTime: this.connection().departureTime,
        endTime: this.connection().arrivalTime,
        description: this.buildScheduleDescription(),
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

}
