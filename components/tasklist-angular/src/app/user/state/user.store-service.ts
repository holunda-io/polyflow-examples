import { Injectable } from '@angular/core';
import { dispatch, select, StoreService } from '@ngxp/store-service';
import { UserState } from './user.reducer';
import { availableUsers, currentUserId, currentUserProfile } from './user.selectors';
import { loadAvailableUsers, selectUser } from './user.actions';
import { first } from 'rxjs/operators';

@Injectable()
export class UserStoreService extends StoreService<UserState> {

  availableUsers$ = select(availableUsers);

  userId$ = select(currentUserId);

  currentUserProfile$ = select(currentUserProfile);

  loadAvailableUsers = dispatch(loadAvailableUsers);

  selectUser = dispatch(selectUser)

  loadInitialUser(): void {
    this.userId$().pipe(first()).subscribe(userId => this.selectUser({userId}));
  }
}
