import { Injectable } from '@angular/core';
import { TaskService } from 'tasklist/services';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { UserStoreService } from 'app/user/state/user.store-service';
import {
  ClaimTaskAction,
  LoadTasksAction,
  PageSelectedAction,
  SelectPageAction,
  TaskActionTypes,
  TaskClaimedAction,
  TasksLoadedAction,
  TaskUnclaimedAction,
  UnclaimTaskAction
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
    map(() => new LoadTasksAction())
  ));

  loadTasksOnSortingChange = createEffect(() => this.actions$.pipe(
    ofType(TaskActionTypes.UpdateSortingColumn),
    map(() => new LoadTasksAction())
  ));

  loadTasks$ = createEffect(() => this.actions$.pipe(
    ofType(TaskActionTypes.LoadTasks),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([_, userId]) =>
      this.taskService.getTasks$Response({
        'X-Current-User-ID': userId
      })),
    map((response) => new TasksLoadedAction({
      tasks: response.body,
      totalCount: Number(response.headers.get('X-ElementCount'))
    })),
    catchError(err => {
      console.log('Error loading tasks:', err);
      return of();
    })
  ));

  selectPage$ = createEffect(() => this.actions$.pipe(
    ofType<SelectPageAction>(TaskActionTypes.SelectPage),
    map(action => action.payload),
    withLatestFrom(this.taskStore.selectedPage$()),
    filter(([newPage, currentPage]) => newPage !== currentPage),
    map(([newPage, _]) => new PageSelectedAction(newPage))
  ));

  claimTask$ = createEffect(() => this.actions$.pipe(
    ofType<ClaimTaskAction>(TaskActionTypes.ClaimTask),
    map(action => action.payload),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([task, userId]) => this.taskService.claim({taskId: task.id, 'X-Current-User-ID': userId})),
    map(() => new TaskClaimedAction()),
    catchError(err => {
      console.log('Error while claiming task', err);
      return of();
    })
  ));

  unclaimTask$ = createEffect(() => this.actions$.pipe(
    ofType<UnclaimTaskAction>(TaskActionTypes.UnclaimTask),
    map(action => action.payload),
    withLatestFrom(this.userStore.userId$()),
    mergeMap(([task, userId]) => this.taskService.unclaim({taskId: task.id, 'X-Current-User-ID': userId})),
    map(() => new TaskUnclaimedAction()),
    catchError(err => {
      console.log('Error while unclaiming task', err);
      return of();
    })
  ));

  reloadTasks$ = createEffect(() => this.actions$.pipe(
    ofType(TaskActionTypes.TaskClaimed, TaskActionTypes.TaskUnclaimed),
    map(() => new LoadTasksAction())
  ));
}
