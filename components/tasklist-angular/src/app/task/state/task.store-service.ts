import { Injectable } from '@angular/core';
import { dispatch, select, StoreService } from '@ngxp/store-service';
import { claimTask, loadTasks, selectPage, unclaimTask, updateSortingColumn } from './task.actions';
import { TaskState } from 'app/task/state/task.reducer';
import { getCount, getSelectedPage, getSortingColumn, getTasks } from 'app/task/state/task.selectors';

@Injectable()
export class TaskStoreService extends StoreService<TaskState> {

  loadTasks = dispatch(loadTasks);

  updateSortingColumn = dispatch(updateSortingColumn);

  selectPage = dispatch(selectPage);

  claim = dispatch(claimTask);

  unclaim = dispatch(unclaimTask);

  tasks = select(getTasks);

  sortingColumn$ = select(getSortingColumn);

  taskCount$ = select(getCount);

  selectedPage$ = select(getSelectedPage);
}
