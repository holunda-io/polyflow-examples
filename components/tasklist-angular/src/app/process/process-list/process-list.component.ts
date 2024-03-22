import { Component } from '@angular/core';
import { ProcessStoreService } from 'app/process/state/process.store-service';

@Component({
  selector: 'tasks-process-list',
  templateUrl: './process-list.component.html',
  styleUrls: ['process-list.component.scss']
})
export class ProcesslistComponent {

  processes$ = this.processStore.startableProcesses$;

  constructor(
    private processStore: ProcessStoreService
  ) {
  }
}
