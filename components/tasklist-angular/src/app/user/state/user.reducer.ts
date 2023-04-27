import { availableUsersLoaded, selectUser, userProfileLoaded } from './user.actions';
import { UserInfo } from 'tasklist/models/user-info';
import { createReducer, on } from "@ngrx/store";

export interface UserProfile {
  userIdentifier: string;
  username: string;
  fullName: string;
}

export interface UserState {
  currentUserId: string;
  availableUsers: UserInfo[];
  currentUserProfile: UserProfile;
}

const initialState: UserState = {
  currentUserId: null,
  availableUsers: [],
  currentUserProfile: {
    userIdentifier: '',
    username: '',
    fullName: ''
  }
};

export const userReducer = createReducer(
  initialState,
  on(availableUsersLoaded, (state, action) => ({
    ...state,
    availableUsers: action.users
  })),
  on(selectUser, (state, action) => ({
    ...state,
    currentUserId: action.userId
  })),
  on(userProfileLoaded, (state, action) => ({
    ...state,
    currentUserProfile: action.profile
  }))
);
