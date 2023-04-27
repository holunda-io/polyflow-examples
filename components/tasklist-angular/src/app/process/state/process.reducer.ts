import { startableProcessDefinitionsLoaded } from './process.actions';
import { createReducer, on } from "@ngrx/store";

export interface ProcessDefinition {
  name: string;
  description: string;
  url: string;
}

export interface ProcessState {
  startableProcesses: ProcessDefinition[];
}

const initialState: ProcessState = {
  startableProcesses: []
};

export const processReducer = createReducer(
  initialState,
  on(startableProcessDefinitionsLoaded, (state, action) => ({
    ...state,
    startableProcesses: action.definitions
  }))
);
