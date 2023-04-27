import { availableUsersLoaded, loadAvailableUsers, selectUser, userProfileLoaded } from './user.actions';
import { UserProfile, userReducer, UserState } from './user.reducer';

describe('userReducer', () => {

  const initialState: UserState = {
    currentUserId: null,
    currentUserProfile: {
      fullName: '',
      username: '',
      userIdentifier: ''
    },
    availableUsers: []
  };

  it('updates available users', () => {
    // given:
    const users = [{ id: '1', username: 'foo' }, { id: '2', username: 'bar' }];
    const action = availableUsersLoaded({users});

    // when:
    const newState = userReducer(initialState, action);

    // then:
    expect(newState.availableUsers).toEqual(users);
  });

  it('updates current user', () => {
    // given:
    const currentUser: UserProfile = {
      username: 'foo',
      userIdentifier: 'bar',
      fullName: 'foobar'
    };
    const action = userProfileLoaded({profile: currentUser});

    // when:
    const newState = userReducer(initialState, action);

    // then:
    expect(newState.currentUserProfile).toEqual(currentUser);
  });

  it('ignores other actions', () => {
    // given:
    const action = loadAvailableUsers();

    // when:
    const newState = userReducer(initialState, action);

    // then:
    expect(newState).toEqual(initialState);
  });

  it('updates current userId', () => {
    // given:
    const action = selectUser({userId: 'kermit'});

    // when:
    const newState = userReducer(initialState, action);

    // then:
    expect(newState.currentUserId).toEqual('kermit');
  });
});
