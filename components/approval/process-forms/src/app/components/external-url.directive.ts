import { Directive, HostListener, ElementRef, inject } from '@angular/core';
import { Router } from '@angular/router';

@Directive({
    selector: 'a[appExternalUrl]',
    standalone: false
})
export class ExternalUrlDirective {
    private el = inject(ElementRef);
    private router = inject(Router);


    @HostListener('click', ['$event'])
    clicked(event: Event) {
        const url = this.el.nativeElement.href;
        if (url === undefined || url === '') {
            return;
        }

        this.router.navigate(['/externalRedirect', { externalUrl: url }], {
            skipLocationChange: true,
        });

        event.preventDefault();
    }
}
