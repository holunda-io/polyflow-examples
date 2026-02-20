import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { loadAvailableUsers, selectUser } from './user.actions';
import { availableUsers, currentUserId, currentUserProfile, StateWithUsers } from './user.selectors';

@Injectable()
export class UserStoreService {
  constructor(
    private readonly store: Store<StateWithUsers>
  ) {
  }

  availableUsers$ = this.store.select(availableUsers);

  userId$ = this.store.select(currentUserId);

  currentUserProfile$ = this.store.select(currentUserProfile);

  loadAvailableUsers() {
    this.store.dispatch(loadAvailableUsers());
  }

  selectUser(userId: string) {
    this.store.dispatch(selectUser({ userId }))
  }
}
