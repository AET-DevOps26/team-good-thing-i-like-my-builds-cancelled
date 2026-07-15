export * from './logbook.service';
import { LogbookService } from './logbook.service';
export * from './route.service';
import { RouteService } from './route.service';
export * from './suggestion.service';
import { SuggestionService } from './suggestion.service';
export const APIS = [LogbookService, RouteService, SuggestionService];
