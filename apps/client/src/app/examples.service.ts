import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Example {
  id: number;
  name: string;
}

export interface ExampleCreateRequest {
  name: string;
}

@Injectable({ providedIn: 'root' })
export class ExamplesService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/examples';

  getAll(): Observable<Example[]> {
    return this.http.get<Example[]>(this.baseUrl);
  }

  getById(id: number): Observable<Example> {
    return this.http.get<Example>(`${this.baseUrl}/${id}`);
  }

  create(request: ExampleCreateRequest): Observable<Example> {
    return this.http.post<Example>(this.baseUrl, request);
  }
}
