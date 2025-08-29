import { Component, inject } from '@angular/core';
import { ProcessStoreService } from 'app/process/state/process.store-service';

@Component({
    selector: 'tasks-process-list',
    templateUrl: './process-list.component.html',
    styleUrls: ['process-list.component.scss'],
    standalone: false
})
export class ProcesslistComponent {
  private processStore = inject(ProcessStoreService);


  processes$ = this.processStore.startableProcesses$;
}
