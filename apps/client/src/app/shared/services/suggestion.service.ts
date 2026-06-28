import { Injectable, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';

export type SuggestionEvent =
  | { type: 'token'; token: string }
  | { type: 'done' };

@Injectable({ providedIn: 'root' })
export class SuggestionService implements OnDestroy {
  private ws: WebSocket | null = null;
  readonly events$ = new Subject<SuggestionEvent>();

  connect(): void {
    if (this.ws) return;
    const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
    this.ws = new WebSocket(`${protocol}://${location.host}/api/v1/suggestion`);

    this.ws.onmessage = (event) => {
      try {
        const msg = JSON.parse(event.data) as SuggestionEvent;
        this.events$.next(msg);
      } catch {}
    };

    this.ws.onclose = () => {
      this.ws = null;
    };
  }

  sendTextUpdate(textBefore: string, textAfter: string): void {
    this.sendIfOpen({ type: 'text_update', textBefore, textAfter });
  }

  sendCancel(): void {
    this.sendIfOpen({ type: 'cancel' });
  }

  private sendIfOpen(payload: object): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(payload));
    }
  }

  ngOnDestroy(): void {
    this.ws?.close();
  }
}
