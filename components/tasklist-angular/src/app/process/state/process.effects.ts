import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { loadStartableProcessDefinitions, startableProcessDefinitionsLoaded } from 'app/process/state/process.actions';
import { ProcessDefinition } from 'app/process/state/process.reducer';
import { selectUser } from 'app/user/state/user.actions';
import { UserStoreService } from 'app/user/state/user.store-service';
import { filter, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { ProcessDefinition as ProcessDto } from 'tasklist/models';
import { ProcessService } from 'tasklist/services';

@Injectable()
export class ProcessEffects {
  private processService = inject(ProcessService);
  private userStore = inject(UserStoreService);
  private actions$ = inject(Actions);


  loadProcessesOnUserSelect = createEffect(() => this.actions$.pipe(
    ofType(selectUser),
    filter(action => !!action.userId),
    map(() => loadStartableProcessDefinitions())
  ));

  loadStartableProcesses$ = createEffect(() => this.actions$.pipe(
    ofType(loadStartableProcessDefinitions),
    withLatestFrom(this.userStore.userId$),
    mergeMap(([_, userId]) => this.processService.getStartableProcesses({
      'X-Current-User-ID': userId
    })),
    map(procDtos => mapFromDto(procDtos)),
    map(definitions => startableProcessDefinitionsLoaded({ definitions }))
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
