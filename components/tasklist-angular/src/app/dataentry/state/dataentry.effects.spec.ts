import { Actions } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { createMockStore } from '@ngrx/store/testing';
import { dataEntriesLoaded, loadDataEntries } from 'app/dataentry/state/dataentry.actions';
import { DataEntry } from 'app/dataentry/state/dataentry.reducer';
import { currentUserId } from 'app/user/state/user.selectors';
import { UserStoreService } from 'app/user/state/user.store-service';
import { of } from 'rxjs';
import { BusinessDataService } from 'tasklist/services';
import { DataentryEffects } from './dataentry.effects';

describe('DataEntryEffects', () => {

  let businessDataService: BusinessDataService;
  let userStore: UserStoreService;

  beforeEach(() => {
    businessDataService = new BusinessDataService(null, null);
    // default user store to be overridden in test if needed.
    userStore = new UserStoreService(createMockStore({
      selectors: [
        { selector: currentUserId, value: 'kermit' }
      ]
    }))
  });

  function effectsFor(action: Action): DataentryEffects {
    return new DataentryEffects(businessDataService, userStore, new Actions(of(action)));
  }

  it('should load available users', (done) => {
    // given:
    const action = loadDataEntries();
    const dataEntriesDtos: Array<DataEntry> = [
      { name: 'foo', description: '', url: '', type: 'type', payload: {}, currentState: 'MY STATE', currentStateType: '', protocol: [] },
      { name: 'bar', description: '', url: '', type: 'type2', payload: {}, currentState: 'MY STATE2', currentStateType: '', protocol: [] }
    ];
    const serviceSpy = spyOn(businessDataService, 'getBusinessDataEntries$Response')
      .and.returnValue(of({ body: dataEntriesDtos, headers: {} } as any));

    // when:
    effectsFor(action).loadDataEntries$.subscribe((newAction) => {
      expect(newAction).toEqual(dataEntriesLoaded({
        dataEntries: [
          { name: 'foo', description: '', url: '', type: 'type', payload: {}, currentState: 'MY STATE', currentStateType: '', protocol: [] },
          { name: 'bar', description: '', url: '', type: 'type2', payload: {}, currentState: 'MY STATE2', currentStateType: '', protocol: [] }
        ]
      }));
      done();
    });
  });
});
