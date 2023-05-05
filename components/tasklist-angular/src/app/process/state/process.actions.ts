import { createAction, props } from '@ngrx/store';
import { ProcessDefinition } from 'app/process/state/process.reducer';

export const loadStartableProcessDefinitions = createAction('[Process] Load startable processes');

export const startableProcessDefinitionsLoaded = createAction('[Process] Startable processes loaded', props<{
  definitions: ProcessDefinition[]
}>());
