import { Injectable, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { loadDataEntries } from './dataentry.actions';
import { dataEntries, StateWithDataEntries } from './dataentry.selectors';

@Injectable()
export class DataentryStoreService {
  private store = inject<Store<StateWithDataEntries>>(Store);


  dataEntries$ = this.store.select(dataEntries);

  loadDataEntries() {
    this.store.dispatch(loadDataEntries());
  }
}
