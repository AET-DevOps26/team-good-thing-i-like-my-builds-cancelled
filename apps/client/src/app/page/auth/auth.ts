import { Component } from '@angular/core';
import {NoContent} from '../../component/no-content/no-content';

@Component({
  selector: 'app-auth',
  imports: [
    NoContent
  ],
  templateUrl: './auth.html',
  styleUrl: './auth.scss',
})
export class Auth {}
