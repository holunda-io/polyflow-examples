import { Injectable } from '@angular/core';
import { dispatch, select, StoreService } from '@ngxp/store-service';
import { DataEntryState } from './dataentry.reducer';
import { dataEntries } from './dataentry.selectors';
import { loadDataEntries } from './dataentry.actions';

@Injectable()
export class DataentryStoreService extends StoreService<DataEntryState> {

  dataEntries$ = select(dataEntries);

  loadDataEntries = dispatch(loadDataEntries);
}
