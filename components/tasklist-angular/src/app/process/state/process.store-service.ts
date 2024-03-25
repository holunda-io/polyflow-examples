import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { loadStartableProcessDefinitions } from './process.actions';
import { StateWithProcesses, startableProcesses } from './process.selectors';

@Injectable()
export class ProcessStoreService {

  constructor(
    private store: Store<StateWithProcesses>
  ) { }

  startableProcesses$ = this.store.select(startableProcesses);

  loadStartableProcessDefinitions() {
    this.store.dispatch(loadStartableProcessDefinitions());
  }
}
