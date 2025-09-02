import { Directive, ElementRef, HostListener, OnDestroy, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UserStoreService } from 'app/user/state/user.store-service';
import { Subscription } from 'rxjs';

@Directive({ selector: 'a[tasksExternalUrl]' })
export class ExternalUrlDirective implements OnDestroy {
  private el = inject(ElementRef);
  private router = inject(Router);
  private userStore = inject(UserStoreService);


  private userId: string;
  private _sub: Subscription;

  constructor() {
    this._sub = this.userStore.userId$.subscribe(userId => this.userId = userId);
  }

  ngOnDestroy(): void {
    this._sub.unsubscribe();
  }

  @HostListener('click', ['$event'])
  clicked(event: Event) {
    const url = this.el.nativeElement.href;
    if (url === undefined || url === '') {
      return;
    }
    const parsedUrl = url.replace('%userId%', this.userId);

    this.router.navigate(['/externalRedirect', { externalUrl: parsedUrl }], {
      skipLocationChange: true,
    });

    event.preventDefault();
  }
}
