import { Actions } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { createMockStore } from '@ngrx/store/testing';
import { loadStartableProcessDefinitions } from 'app/process/state/process.actions';
import { currentUserId } from 'app/user/state/user.selectors';
import { UserStoreService } from 'app/user/state/user.store-service';
import { of } from 'rxjs';
import { ProcessDefinition as ApiProcessDefinition } from 'tasklist/models/process-definition';
import { ProcessService } from 'tasklist/services';
import { ProcessEffects } from './process.effects';

describe('ProcessEffects', () => {

  let processService: ProcessService;
  let userStore: UserStoreService;

  beforeEach(() => {
    processService = new ProcessService(null, null);
    // default user store to be overridden in test if needed.
    userStore = new UserStoreService(createMockStore({
      selectors: [
        { selector: currentUserId, value: 'kermit' }
      ]
    }));
  });

  function effectsFor(action: Action): ProcessEffects {
    return new ProcessEffects(processService, userStore, new Actions(of(action)));
  }

  it('should load available process definitions', (done) => {
    // given:
    const action = loadStartableProcessDefinitions();
    const procDtos: ApiProcessDefinition[] = [
      {
        processName: 'foo', description: '', url: '', candidateGroups: [], candidateUsers: [], definitionId: 'foo-id',
        definitionKey: 'foo-key', definitionVersion: '', versionTag: '1'
      },
      {
        processName: 'bar', description: '', url: '', candidateGroups: [], candidateUsers: [], definitionId: 'foo-id',
        definitionKey: 'foo-key', definitionVersion: '', versionTag: '2'
      }
    ];
    const serviceSpy = spyOn(processService, 'getStartableProcesses').and.returnValue(of(procDtos));

    // when:
    effectsFor(action).loadStartableProcesses$.subscribe((newAction) => {
      expect(serviceSpy).toHaveBeenCalled();
      expect(newAction.definitions).toEqual([
        { name: 'foo', description: '', url: '' },
        { name: 'bar', description: '', url: '' }
      ]);
      done();
    });
  });

});
