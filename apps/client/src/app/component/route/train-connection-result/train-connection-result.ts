import {Component, computed, input} from '@angular/core';
import {TrainConnection} from '@/generated';
import {ZardAccordionItemComponent} from '@/shared/components/accordion';

@Component({
  selector: 'app-train-connection-result',
  imports: [
    ZardAccordionItemComponent
  ],
  templateUrl: './train-connection-result.html',
  styleUrl: './train-connection-result.scss',
})
export class TrainConnectionResult {
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

}
