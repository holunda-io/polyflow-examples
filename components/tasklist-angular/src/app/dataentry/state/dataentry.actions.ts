import { createAction, props } from '@ngrx/store';
import { DataEntry } from 'app/dataentry/state/dataentry.reducer';

export const loadDataEntries = createAction('[Data Entry] Load');

export const dataEntriesLoaded = createAction('[Data Entry] Loaded', props<{
  dataEntries: DataEntry[]
}>());
