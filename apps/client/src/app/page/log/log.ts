import {
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { ZardButtonComponent } from '@/shared/components/button';
import { SuggestionService } from '@/shared/services/suggestion.service';

@Component({
  selector: 'app-log',
  imports: [ZardButtonComponent],
  templateUrl: './log.html',
  styleUrl: './log.scss',
})
export class Log implements OnInit, OnDestroy {
  @ViewChild('editor') editorRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('overlay') overlayRef!: ElementRef<HTMLDivElement>;

  private suggestion = inject(SuggestionService);
  private sub!: Subscription;
  private debounceTimer: ReturnType<typeof setTimeout> | null = null;

  readonly reportText = signal('');
  readonly ghostText = signal('');
  readonly isStreaming = signal(false);

  /** Text shown in the textarea placeholder area */
  readonly placeholder = 'Schreibe deinen Reisebericht hier...';

  ngOnInit(): void {
    this.suggestion.connect();
    this.sub = this.suggestion.events$.subscribe((event) => {
      if (event.type === 'token') {
        this.ghostText.update((g) => g + event.token);
      } else if (event.type === 'done') {
        this.isStreaming.set(false);
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    if (this.debounceTimer) clearTimeout(this.debounceTimer);
  }

  onScroll(event: Event): void {
    const overlay = this.overlayRef?.nativeElement;
    if (overlay) overlay.scrollTop = (event.target as HTMLTextAreaElement).scrollTop;
  }

  onInput(event: Event): void {
    const el = event.target as HTMLTextAreaElement;
    const text = el.value;
    this.reportText.set(text);

    this.ghostText.set('');
    this.isStreaming.set(false);
    if (this.debounceTimer) clearTimeout(this.debounceTimer);

    if (!text.trim()) return;

    this.debounceTimer = setTimeout(() => {
      const cursor = el.selectionStart ?? text.length;
      const textBefore = text.slice(0, cursor);
      const textAfter = text.slice(cursor);

      this.isStreaming.set(true);
      this.suggestion.sendTextUpdate(textBefore, textAfter);
    }, 600);
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Tab' && this.ghostText()) {
      event.preventDefault();
      this.acceptSuggestion();
      return;
    }
    if (event.key === 'Escape' && this.ghostText()) {
      this.discardSuggestion();
      return;
    }
  }

  acceptSuggestion(): void {
    const ghost = this.ghostText();
    if (!ghost) return;
    const el = this.editorRef?.nativeElement;
    const cursor = el?.selectionStart ?? this.reportText().length;
    const before = this.reportText().slice(0, cursor);
    const after = this.reportText().slice(cursor);
    this.reportText.set(before + ghost + after);
    this.ghostText.set('');
    this.isStreaming.set(false);
    this.suggestion.sendCancel();
    // Move cursor to end of inserted text
    const newCursor = cursor + ghost.length;
    setTimeout(() => {
      el?.setSelectionRange(newCursor, newCursor);
      el?.focus();
    });
  }

  discardSuggestion(): void {
    this.ghostText.set('');
    this.isStreaming.set(false);
    this.suggestion.sendCancel();
  }

  clearReport(): void {
    this.reportText.set('');
    this.ghostText.set('');
    this.isStreaming.set(false);
    this.suggestion.sendCancel();
  }
}
