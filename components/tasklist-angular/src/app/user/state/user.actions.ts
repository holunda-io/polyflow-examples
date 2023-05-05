import { createAction, props } from '@ngrx/store';
import { UserProfile } from './user.reducer';
import { UserInfo } from 'tasklist/models/user-info';


export const loadAvailableUsers = createAction('[User] Load available ids');

export const availableUsersLoaded = createAction('[User] Available ids loaded', props<{
  users: UserInfo[]
}>());

export const selectUser = createAction('[User] Select user', props<{
  userId: string
}>());

export const loadUserProfile = createAction('[User] Load profile', props<{
  userId: string
}>());

export const userProfileLoaded = createAction('[User] Profile loaded', props<{
  profile: UserProfile
}>());
