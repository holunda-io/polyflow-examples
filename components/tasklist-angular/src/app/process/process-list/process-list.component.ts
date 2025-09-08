import { Component, inject } from '@angular/core';
import { ProcessStoreService } from 'app/process/state/process.store-service';
import {NgbDropdown, NgbDropdownMenu, NgbDropdownToggle} from '@ng-bootstrap/ng-bootstrap';
import { AsyncPipe } from '@angular/common';
import { ExternalUrlDirective } from 'app/shared/external-url.directive';

@Component({
    selector: 'tasks-process-list',
    templateUrl: './process-list.component.html',
    styleUrls: ['process-list.component.scss'],
    imports: [NgbDropdownMenu, NgbDropdown, NgbDropdownToggle, ExternalUrlDirective, AsyncPipe]
})
export class ProcesslistComponent {
  private processStore = inject(ProcessStoreService);


  processes$ = this.processStore.startableProcesses$;
}
