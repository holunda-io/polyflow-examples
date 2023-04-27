import { Injectable } from '@angular/core';
import { ProfileService } from 'tasklist/services';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
  availableUsersLoaded,
  loadAvailableUsers,
  loadUserProfile,
  selectUser,
  userProfileLoaded
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
    ofType(loadAvailableUsers),
    mergeMap(() => this.profileService.getUsers()),
    map((users: UserInfo[]) => availableUsersLoaded({users}))
  ));

  loadInitialUser$ = createEffect(() =>this.actions$.pipe(
    ofType(availableUsersLoaded),
    withLatestFrom(this.userStore.userId$()),
    filter(([_, userId]) => !userId),
    map(([action]) => action.users),
    map((users) => selectUser({userId: users[0].id}))
  ));

  selectUser$ = createEffect(() => this.actions$.pipe(
    ofType(selectUser),
    map((action) => action.userId),
    filter(userId => !!userId),
    map(userId => loadUserProfile({userId}))
  ));

  loadUserProfile$ = createEffect(() => this.actions$.pipe(
    ofType(loadUserProfile),
    map((action) => action.userId),
    mergeMap((userId) => this.profileService.getProfile({ 'X-Current-User-ID': userId }).pipe(
      map((profile) => mapFromDto(profile, userId))
    )),
    map((profile) => userProfileLoaded({profile}))
  ));
}

function mapFromDto(profile: UserDto, userId: string): UserProfile {
  return {
    userIdentifier: userId,
    username: profile.username,
    fullName: new TitleCasePipe().transform(profile.username)
  };
}
