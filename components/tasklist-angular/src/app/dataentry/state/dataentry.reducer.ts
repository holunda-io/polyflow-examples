import { dataEntriesLoaded } from './dataentry.actions';
import { createReducer, on } from "@ngrx/store";

export interface DataEntry {
  name: string;
  description?: string;
  type: string;
  payload: {};
  url: string;
  currentState: string;
  currentStateType: string;
  protocol: ProtocolEntry[];
}

export interface ProtocolEntry {
  timestamp?: string;
  user?: string;
  state?: string;
  stateType?: string;
  log?: string;
  logDetails?: string;
}

export interface DataEntryState {
  dataEntries: DataEntry[];
}

const initialState: DataEntryState = {
  dataEntries: []
};

export const dataentryReducer = createReducer(
  initialState,
  on(dataEntriesLoaded, (state, action) => ({
    ...state,
    dataEntries: action.dataEntries
  }))
);
