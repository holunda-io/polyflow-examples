import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { loadDataEntries } from './dataentry.actions';
import { dataEntries, StateWithDataEntries } from './dataentry.selectors';

@Injectable()
export class DataentryStoreService {

  constructor(
    private store: Store<StateWithDataEntries>
  ) { }

  dataEntries$ = this.store.select(dataEntries);

  loadDataEntries() {
    this.store.dispatch(loadDataEntries());
  }
}
