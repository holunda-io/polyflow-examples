import { Component, inject } from '@angular/core';
import { DataentryStoreService } from 'app/dataentry/state/dataentry.store-service';

@Component({
    selector: 'tasks-data-entry-list',
    templateUrl: './dataentry-list.component.html',
    styleUrls: ['dataentry-list.component.scss'],
    standalone: false
})
export class DataentryListComponent {
  private dataEntryStore = inject(DataentryStoreService);


  dataEntries$ = this.dataEntryStore.dataEntries$;
  currentDataTab = 'description';
  itemsPerPage: number;
  totalItems: any;
  page: number;

  toFieldSet(payload: any) {
    const payloadProps = Object.keys(payload);
    const result = [];
    for (const prop of payloadProps) {
      result.push({ name: prop, value: payload[prop] });
    }
    return result;
  }

  reload() {
    console.log('Reload');
  }

  loadPage() {
    console.log('Load page');
  }
}
