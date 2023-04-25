import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { UserStoreService } from 'app/user/state/user.store-service';
import { ProcessService } from 'tasklist/services';
import {
  LoadStartableProcessDefinitions,
  ProcessActionTypes,
  StartableProcessDefinitionsLoaded
} from 'app/process/state/process.actions';
import { filter, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { ProcessDefinition as ProcessDto } from 'tasklist/models';
import { ProcessDefinition } from 'app/process/state/process.reducer';
import { SelectUserAction, UserActionTypes } from 'app/user/state/user.actions';

@Injectable()
export class ProcessEffects {

  public constructor(
    private processService: ProcessService,
    private userStore: UserStoreService,
    private actions$: Actions) {
  }

  loadProcessesOnUserSelect = createEffect(() => this.actions$.pipe(
    ofType<SelectUserAction>(UserActionTypes.SelectUser),
    filter(action => !!action.payload),
    map(() => new LoadStartableProcessDefinitions())
  ));

  loadStartableProcesses$ = createEffect(() => this.actions$.pipe(
    ofType(ProcessActionTypes.LoadStartableProcesses),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([_, userId]) => this.processService.getStartableProcesses({
      'X-Current-User-ID': userId
    })),
    map(procDtos => mapFromDto(procDtos)),
    map(procDefs => new StartableProcessDefinitionsLoaded(procDefs))
  ));
}

function mapFromDto(processDtos: ProcessDto[]): ProcessDefinition[] {
  return processDtos.map(dto => {
    return {
      name: dto.processName,
      url: dto.url,
      description: dto.description
    };
  });
}
