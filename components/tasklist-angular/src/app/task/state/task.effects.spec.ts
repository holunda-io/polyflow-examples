import { TaskEffects } from './task.effects';
import { Action } from '@ngrx/store';
import { of } from 'rxjs';
import { Actions } from '@ngrx/effects';
import { TaskService } from 'tasklist/services';
import { UserStoreService } from 'app/user/state/user.store-service';
import { createStoreServiceMock } from '@ngxp/store-service/testing';
import {
  claimTask,
  loadTasks,
  pageSelected,
  selectPage, taskClaimed,
  tasksLoaded,
  taskUnclaimed,
  unclaimTask
} from 'app/task/state/task.actions';
import { selectUser } from 'app/user/state/user.actions';
import { TaskStoreService } from 'app/task/state/task.store-service';
import { Task } from "tasklist/models/task";

describe('TaskEffects', () => {

  let taskService: TaskService;
  let userStore: UserStoreService;
  let taskStore: TaskStoreService;

  beforeEach(() => {
    taskService = new TaskService(null, null);
    // default user store to be overridden in test if needed.
    userStore = createStoreServiceMock(UserStoreService,
      {userId$: 'kermit'});
    taskStore = createStoreServiceMock(TaskStoreService);
  });

  function effectsFor(action: Action): TaskEffects {
    return new TaskEffects(taskService, userStore, taskStore, new Actions(of(action)));
  }

  it('should trigger loading tasks on user select', (done) => {
    // given:
    const action = selectUser({userId: 'kermit'});

    // when:
    effectsFor(action).loadTasksOnUserSelect$.subscribe((newAction) => {
      expect(newAction).toEqual(loadTasks());
      done();
    });
  });

  it('should load tasks', (done) => {
    // given:
    const action = loadTasks();
    const spy = spyOn(taskService, 'getTasks$Response').and.returnValue(of({body: [],
    headers: { get: (field: string) => '0'}} as any));

    // when:
    effectsFor(action).loadTasks$.subscribe(newAction => {
      expect(newAction).toEqual(tasksLoaded({tasks: [], totalCount: 0}));
      expect(spy).toHaveBeenCalled();
      done();
    });
  });

  it('should update selected page', (done) => {
    // given:
    const action = selectPage({pageNumber: 1});
    taskStore = createStoreServiceMock(TaskStoreService, {
      selectedPage$: 0
    });

    effectsFor(action).selectPage$.subscribe((newAction) => {
      expect(newAction).toEqual(pageSelected({pageNumber: 1}));
      done();
    });
  });

  it('should claim task', (done) => {
    // given:
    const task: Task = {
      url: '',
      id: '',
      createTime: '',
      candidateGroups: [],
      candidateUsers: [],
      processName: ''
    };
    const action = claimTask({task});
    spyOn(taskService, 'claim').and.callFake(() => of(undefined as void));

    // when:
    effectsFor(action).claimTask$.subscribe((newAction) => {
      // then:
      expect(taskService.claim).toHaveBeenCalled();
      expect(newAction).toEqual(taskClaimed())
      done();
    })
  });

  it('should unclaim task', (done) => {
    // given:
    const task: Task = {
      url: '',
      id: '',
      createTime: '',
      candidateGroups: [],
      candidateUsers: [],
      processName: ''
    };
    const action = unclaimTask({task});
    spyOn(taskService, 'unclaim').and.callFake(() => of(undefined as void));

    // when:
    effectsFor(action).unclaimTask$.subscribe((newAction) => {
      // then:
      expect(taskService.unclaim).toHaveBeenCalled();
      expect(newAction).toEqual(taskUnclaimed())
      done();
    })
  });
});
