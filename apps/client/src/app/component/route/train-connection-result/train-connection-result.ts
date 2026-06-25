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
  connection = input<TrainConnection>({} as TrainConnection);

  startAt = computed(() => {
    return this.getTimeString(this.connection().departureTime);
  });

  endAt = computed(() => {
    return this.getTimeString(this.connection().arrivalTime);
  });

  getTimeString(dateString: string): string {
    const date = new Date(dateString);
    return `${date.getHours()}:${date.getMinutes()}`;
  }
}
