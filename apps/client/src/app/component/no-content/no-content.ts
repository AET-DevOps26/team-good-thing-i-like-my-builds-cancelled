import {Component, input} from '@angular/core';

@Component({
  selector: 'app-no-content',
  imports: [],
  templateUrl: './no-content.html',
  styleUrl: './no-content.scss',
})
export class NoContent {
  message = input<string>('No Content');
}
