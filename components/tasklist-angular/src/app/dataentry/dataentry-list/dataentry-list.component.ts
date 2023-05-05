import { Component } from '@angular/core';
import { DataentryStoreService } from 'app/dataentry/state/dataentry.store-service';

@Component({
  selector: 'tasks-data-entry-list',
  templateUrl: './dataentry-list.component.html',
  styleUrls: ['dataentry-list.component.scss']
})
export class DataentryListComponent {

  dataEntries$ = this.dataEntryStore.dataEntries$();
  currentDataTab = 'description';
  itemsPerPage: number;
  totalItems: any;
  page: number;

  constructor(
    private dataEntryStore: DataentryStoreService,
  ) {  }

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
