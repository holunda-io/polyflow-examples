import { pageSelected, tasksLoaded, updateSortingColumn } from './task.actions';
import { TaskWithDataEntries } from 'tasklist/models';
import { createReducer, on } from "@ngrx/store";

export enum SortDirection {
  ASC = '+',
  DESC = '-'
}

export interface Field {
  fieldName: string;
  direction: SortDirection;
}

export interface TaskState {
  sortingColumn: Field;
  page: number;
  taskCount: number;
  tasks: TaskWithDataEntries[];
}

const initialState: TaskState = {
  sortingColumn: {fieldName: 'task.dueDate', direction: SortDirection.DESC},
  page: 0,
  taskCount: 0,
  tasks: []
};

export const taskReducer = createReducer(
  initialState,
  on(tasksLoaded, (state, action) => ({
    ...state,
    tasks: action.tasks,
    taskCount: action.totalCount
  })),
  on(pageSelected, (state, action) => ({
    ...state,
    page: action.pageNumber
  })),
  on(updateSortingColumn, (state, action) => ({
    ...state,
    sortingColumn: action.field
  }))
);
