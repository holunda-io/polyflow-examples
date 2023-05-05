import { createAction, props } from '@ngrx/store';
import { TaskWithDataEntries } from 'tasklist/models/task-with-data-entries';
import { Task } from 'tasklist/models/task';
import { Field } from 'app/task/state/task.reducer';

export const loadTasks = createAction('[Task] Load tasks');

export const tasksLoaded = createAction('[Task] Tasks loaded', props<{
  tasks: TaskWithDataEntries[];
  totalCount: number
}>());

export const updateSortingColumn = createAction('[Task] Update sorting column', props<{
  field: Field
}>());

export const selectPage = createAction('[Task] Select page', props<{
  pageNumber: number
}>());

export const pageSelected = createAction('[Task] Page selected', props<{
  pageNumber: number
}>());

export const claimTask = createAction('[Task] Claim', props<{
  task: Task
}>());

export const taskClaimed = createAction('[Task] Claimed');

export const unclaimTask = createAction('[Task] Unclaim', props<{
  task: Task
}>());

export const taskUnclaimed = createAction('[Task] Unclaimed');
