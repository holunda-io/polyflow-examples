import { Injectable } from '@angular/core';
import { TaskService } from 'tasklist/services';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { UserStoreService } from 'app/user/state/user.store-service';
import {
  claimTask,
  loadTasks,
  pageSelected,
  selectPage,
  taskClaimed,
  tasksLoaded,
  taskUnclaimed,
  unclaimTask,
  updateSortingColumn
} from 'app/task/state/task.actions';
import { catchError, filter, map, mergeMap, withLatestFrom } from 'rxjs/operators';
import { selectUser } from 'app/user/state/user.actions';
import { of } from 'rxjs';
import { TaskStoreService } from 'app/task/state/task.store-service';

@Injectable()
export class TaskEffects {
  public constructor(
    private taskService: TaskService,
    private userStore: UserStoreService,
    private taskStore: TaskStoreService,
    private actions$: Actions) {
  }

  loadTasksOnUserSelect$ = createEffect(() => this.actions$.pipe(
    ofType(selectUser),
    filter((action) => !!action.userId),
    map(() => loadTasks())
  ));

  reloadTasks$ = createEffect(() => this.actions$.pipe(
    ofType(taskClaimed, taskUnclaimed, updateSortingColumn),
    map(() => loadTasks())
  ));

  loadTasks$ = createEffect(() => this.actions$.pipe(
    ofType(loadTasks),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([_, userId]) =>
      this.taskService.getTasks$Response({
        'X-Current-User-ID': userId
      })),
    map((response) => tasksLoaded({
      tasks: response.body,
      totalCount: Number(response.headers.get('X-ElementCount'))
    })),
    catchError(err => {
      console.log('Error loading tasks:', err);
      return of();
    })
  ));

  selectPage$ = createEffect(() => this.actions$.pipe(
    ofType(selectPage),
    map(action => action.pageNumber),
    withLatestFrom(this.taskStore.selectedPage$()),
    filter(([newPage, currentPage]) => newPage !== currentPage),
    map(([pageNumber, _]) => pageSelected({pageNumber}))
  ));

  claimTask$ = createEffect(() => this.actions$.pipe(
    ofType(claimTask),
    map(action => action.task),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([task, userId]) => this.taskService.claim({taskId: task.id, 'X-Current-User-ID': userId})),
    map(() => taskClaimed()),
    catchError(err => {
      console.log('Error while claiming task', err);
      return of();
    })
  ));

  unclaimTask$ = createEffect(() => this.actions$.pipe(
    ofType(unclaimTask),
    map(action => action.task),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([task, userId]) => this.taskService.unclaim({taskId: task.id, 'X-Current-User-ID': userId})),
    map(() => taskUnclaimed()),
    catchError(err => {
      console.log('Error while unclaiming task', err);
      return of();
    })
  ));
}
