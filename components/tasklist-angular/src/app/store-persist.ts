import { ActionReducer } from '@ngrx/store';
import { localStorageSync } from 'ngrx-store-localstorage';

export function storePersist(reducer: ActionReducer<any>): ActionReducer<any> {
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
