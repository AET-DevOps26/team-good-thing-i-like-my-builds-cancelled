import { isPlatformBrowser } from '@angular/common';
import {ChangeDetectorRef, Component, inject, OnInit, PLATFORM_ID} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Example, ExampleCreateRequest, ExamplesService } from './examples.service';
import {RouterOutlet} from '@angular/router';
import {Header} from './layout/header/header';
import {Footer} from './layout/footer/footer';

@Component({
  selector: 'app-root',
  imports: [CommonModule, ReactiveFormsModule, RouterOutlet, Header, Footer],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly fb = inject(FormBuilder);
  private readonly examplesService = inject(ExamplesService);

  protected readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]],
  });

  protected examples: Example[] = [];
  protected loading = false;
  protected saving = false;
  protected errorMessage: string | null = null;
  protected successMessage: string | null = null;

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.loadExamples();
    }
  }

  protected loadExamples(): void {
    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;
    this.changeDetectorRef.detectChanges();

    this.examplesService.getAll().subscribe({
      next: (examples) => {
        this.examples = examples;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Could not load examples from the route service.';
        this.changeDetectorRef.detectChanges();
      },
    });
  }

  protected saveExample(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request: ExampleCreateRequest = {
      name: this.form.getRawValue().name.trim(),
    };

    this.saving = true;
    this.errorMessage = null;
    this.successMessage = null;
    this.changeDetectorRef.detectChanges();

    this.examplesService.create(request).subscribe({
      next: () => {
        this.form.reset({ name: '' });
        this.successMessage = 'Example created successfully.';
        this.saving = false;
        this.loadExamples();
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.saving = false;
        this.errorMessage = 'Could not create the example.';
        this.changeDetectorRef.detectChanges();
      },
    });
  }

  protected get nameControl() {
    return this.form.controls.name;
  }
}
