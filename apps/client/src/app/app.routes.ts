import {Routes} from '@angular/router';
import {Plan} from './page/plan/plan';
import {Log} from './page/log/log';
import {Auth} from './page/auth/auth';

export const routes: Routes = [
  {path: '', component: Plan},
  {path: "plan", component: Plan},
  {path: "log", component: Log},
  {path: "auth", component: Auth}
];
