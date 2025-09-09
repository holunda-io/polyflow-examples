import { Component, inject } from '@angular/core';
import { DataentryStoreService } from 'app/dataentry/state/dataentry.store-service';
import { FormsModule } from '@angular/forms';
import { AsyncPipe, DatePipe } from '@angular/common';
import { ExternalUrlDirective } from 'app/shared/external-url.directive';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import { FieldNamePipe } from 'app/shared/field-name.pipe';
import {SortableColumnComponent} from "app/task/sorter/sortable-column.component";

@Component({
    selector: 'tasks-data-entry-list',
    templateUrl: './dataentry-list.component.html',
    styleUrls: ['dataentry-list.component.scss'],
    imports: [FormsModule, ExternalUrlDirective, NgbPagination, AsyncPipe, DatePipe, FieldNamePipe, SortableColumnComponent]
})
export class DataentryListComponent {
  private dataEntryStore = inject(DataentryStoreService);


  dataEntries$ = this.dataEntryStore.dataEntries$;
  currentDataTab = 'description';
  itemsPerPage: number;
  totalItems: number;
  page: number;

  toFieldSet(payload: Record<string, unknown>) {
    const payloadProps = Object.keys(payload);
    const result: {name: string, value: unknown}[] = [];
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
