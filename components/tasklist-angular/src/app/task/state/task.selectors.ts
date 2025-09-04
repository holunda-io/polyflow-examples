import {createSelector} from '@ngrx/store';
import {Field, TaskState} from 'app/task/state/task.reducer';
import {TaskWithDataEntries} from 'tasklist/models/task-with-data-entries';

export interface StateWithTasks {
  task: TaskState;
}

const selectFeature = (state: StateWithTasks) => state.task;

export const getTasks = createSelector(
  selectFeature,
  (state: TaskState): TaskWithDataEntries[] => state.tasks
);

export const getSortingColumn = createSelector(
  selectFeature,
  (state: TaskState): Field => state.sortingColumn
);

export const getCount = createSelector(
  selectFeature,
  (state: TaskState): number => state.taskCount
);

export const itemsPerPage = 7;

export const getSelectedPage = createSelector(
  selectFeature,
  state => state.page
);
