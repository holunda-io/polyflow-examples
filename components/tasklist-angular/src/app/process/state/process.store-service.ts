import { Injectable } from '@angular/core';
import { dispatch, select, StoreService } from '@ngxp/store-service';
import { ProcessState } from './process.reducer';
import { startableProcesses } from './process.selectors';
import { loadStartableProcessDefinitions } from './process.actions';

@Injectable()
export class ProcessStoreService extends StoreService<ProcessState> {

  startableProcesses$ = select(startableProcesses);

  loadStartableProcessDefinitions = dispatch(loadStartableProcessDefinitions);
}
