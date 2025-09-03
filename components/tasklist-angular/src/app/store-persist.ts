import { ActionReducer } from '@ngrx/store';
import { localStorageSync } from 'ngrx-store-localstorage';

export function storePersist(reducer: ActionReducer<unknown>): ActionReducer<unknown> {
  return localStorageSync({
    keys: [
      {
        'user': [
          'currentUserId',
          'currentUserProfile'
        ]
      }
    ],
    rehydrate: true
  })(reducer);
}
