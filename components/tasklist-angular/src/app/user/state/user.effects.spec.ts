import { Actions } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { createMockStore } from '@ngrx/store/testing';
import { loadAvailableUsers, loadUserProfile, selectUser } from 'app/user/state/user.actions';
import { UserProfile } from 'app/user/state/user.reducer';
import { UserStoreService } from 'app/user/state/user.store-service';
import { of } from 'rxjs';
import { ProfileService } from 'tasklist/services';
import { UserEffects } from './user.effects';

describe('UserEffects', () => {

  let profileService: ProfileService;
  let userStore: UserStoreService;

  beforeEach(() => {
    profileService = new ProfileService(null, null);
    // default user store to be overridden in test if needed.
    userStore = new UserStoreService(createMockStore());
  });

  function effectsFor(action: Action): UserEffects {
    return new UserEffects(profileService, userStore, new Actions(of(action)));
  }

  it('should load available users', (done) => {
    // given:
    const action = loadAvailableUsers();
    const usersList = [{ id: '1', username: 'foo' }, { id: '2', username: 'bar' }];
    const serviceSpy = spyOn(profileService, 'getUsers').and.returnValue(of(usersList));

    // when:
    effectsFor(action).loadAvailableUserIds$.subscribe((newAction) => {
      expect(serviceSpy).toHaveBeenCalled();
      expect(newAction.users).toEqual(usersList);
      done();
    });
  });

  it('should trigger a user load on user selection', (done) => {
    // given:
    const userId = 'foo';
    const action = selectUser({ userId });

    // when:
    effectsFor(action).selectUser$.subscribe(newAction => {
      expect(newAction).toEqual(loadUserProfile({ userId }));
      done();
    });
  });

  it('should load a user profile', (done) => {
    // given:
    const userId = 'foo';
    const user: UserProfile = {
      username: 'foo',
      userIdentifier: 'foo',
      fullName: 'Foo'
    };
    const action = loadUserProfile({ userId });
    spyOn(profileService, 'getProfile').and.returnValue(of(user));

    // when:
    effectsFor(action).loadUserProfile$.subscribe((newAction) => {
      expect(newAction.profile).toEqual(user);
      done();
    });
  });
});
