import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { StateWithTasks, getCount, getSelectedPage, getSortingColumn, getTasks } from 'app/task/state/task.selectors';
import { Task } from 'tasklist/models/task';
import { claimTask, loadTasks, selectPage, unclaimTask, updateSortingColumn } from './task.actions';
import { Field } from './task.reducer';

@Injectable()
export class TaskStoreService {

  constructor(
    private store: Store<StateWithTasks>
  ) { }

  loadTasks() {
    this.store.dispatch(loadTasks());
  }

  updateSortingColumn(field: Field) {
    this.store.dispatch(updateSortingColumn({ field }));
  }

  selectPage(pageNumber: number) {
    this.store.dispatch(selectPage({ pageNumber }));
  }

  claim(task: Task) {
    this.store.dispatch(claimTask({ task }));
  }

  unclaim(task: Task) {
    this.store.dispatch(unclaimTask({ task }));
  }

  tasks$ = this.store.select(getTasks);

  sortingColumn$ = this.store.select(getSortingColumn);

  taskCount$ = this.store.select(getCount);

  selectedPage$ = this.store.select(getSelectedPage);
}
