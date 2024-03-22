import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { dataEntriesLoaded, loadDataEntries } from 'app/dataentry/state/dataentry.actions';
import { DataEntry } from 'app/dataentry/state/dataentry.reducer';
import { selectUser } from 'app/user/state/user.actions';
import { UserStoreService } from 'app/user/state/user.store-service';
import { of } from 'rxjs';
import { catchError, filter, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { DataEntry as DataEntryDto } from 'tasklist/models';
import { BusinessDataService } from 'tasklist/services';
import { StrictHttpResponse } from 'tasklist/strict-http-response';

@Injectable()
export class DataentryEffects {

  public constructor(
    private businessDataService: BusinessDataService,
    private userStore: UserStoreService,
    private actions$: Actions) {
  }

  loadDataEntriesOnUserSelect = createEffect(() => this.actions$.pipe(
    ofType(selectUser),
    filter((action) => !!action.userId),
    map(() => loadDataEntries())
  ));

  loadDataEntries$ = createEffect(() => this.actions$.pipe(
    ofType(loadDataEntries),
    withLatestFrom(this.userStore.userId$),
    mergeMap(([_, userId]) => this.businessDataService.getBusinessDataEntries$Response({
      'X-Current-User-ID': userId
    })),
    map(dataEntriesDtos => mapFromDto(dataEntriesDtos)),
    map(dataEntries => dataEntriesLoaded({ dataEntries })),
    catchError(err => {
      console.log('Error loading data entries:', err);
      return of();
    })
  ));
}

function mapFromDto(dataEntryDtos: StrictHttpResponse<DataEntryDto[]>): DataEntry[] {
  return dataEntryDtos.body.map(dto => {
    return {
      name: dto.name,
      url: dto.url,
      description: dto.description,
      type: dto.type,
      payload: dto.payload,
      currentState: dto.currentState,
      currentStateType: dto.currentStateType,
      protocol: dto.protocol
    };
  });
}
