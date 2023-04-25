import { Injectable } from '@angular/core';
import { ProfileService } from 'tasklist/services';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
  AvailableUsersLoadedAction,
  LoadUserProfileAction,
  SelectUserAction,
  UserActionTypes,
  UserProfileLoadedAction
} from './user.actions';
import { filter, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { UserInfo, UserProfile as UserDto } from 'tasklist/models';
import { UserProfile } from './user.reducer';
import { TitleCasePipe } from '@angular/common';
import { UserStoreService } from 'app/user/state/user.store-service';

@Injectable()
export class UserEffects {

  public constructor(
    private profileService: ProfileService,
    private userStore: UserStoreService,
    private actions$: Actions) {
  }

  loadAvailableUserIds$ = createEffect(() => this.actions$.pipe(
    ofType(UserActionTypes.LoadAvailableUsers),
    mergeMap(() => this.profileService.getUsers()),
    map((users: UserInfo[]) => new AvailableUsersLoadedAction(users))
  ));

  loadInitialUser$ = createEffect(() =>this.actions$.pipe(
    ofType<AvailableUsersLoadedAction>(UserActionTypes.AvailableUsersLoaded),
    withLatestFrom(this.userStore.userId$()),
    filter(([_, userId]) => !userId),
    map(([action, _]) => action.payload),
    map((users) => new SelectUserAction(Object.keys(users)[0]))
  ));

  selectUser$ = createEffect(() => this.actions$.pipe(
    ofType(UserActionTypes.SelectUser),
    map((action: SelectUserAction) => action.payload),
    filter(userId => !!userId),
    map(userId => new LoadUserProfileAction(userId))
  ));

  loadUserProfile$ = createEffect(() => this.actions$.pipe(
    ofType(UserActionTypes.LoadUserProfile),
    map((action: LoadUserProfileAction) => action.payload),
    mergeMap((userId) => this.profileService.getProfile({ 'X-Current-User-ID': userId }).pipe(
      map((profile) => mapFromDto(profile, userId))
    )),
    map((profile) => new UserProfileLoadedAction(profile))
  ));
}

function mapFromDto(profile: UserDto, userId: string): UserProfile {
  return {
    userIdentifier: userId,
    username: profile.username,
    fullName: new TitleCasePipe().transform(profile.username)
  };
}
