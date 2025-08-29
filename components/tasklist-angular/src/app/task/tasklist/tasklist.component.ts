import { Component, inject } from '@angular/core';
import { itemsPerPage } from 'app/task/state/task.selectors';
import { TaskStoreService } from 'app/task/state/task.store-service';
import { UserStoreService } from 'app/user/state/user.store-service';
import { Task } from 'tasklist/models';

@Component({
    selector: 'tasks-tasklist',
    templateUrl: './tasklist.component.html',
    styleUrls: ['tasklist.component.scss'],
    standalone: false
})
export class TasklistComponent {
  private taskStore = inject(TaskStoreService);
  private userStore = inject(UserStoreService);


  currentDataTab = 'description';
  itemsPerPage = itemsPerPage;

  totalItems = this.taskStore.taskCount$;
  page = this.taskStore.selectedPage$;
  currentProfile$ = this.userStore.currentUserProfile$;
  tasks = this.taskStore.tasks$;

  claim($event, task: Task) {
    this.taskStore.claim(task);
  }

  unclaim($event, task: Task) {
    this.taskStore.unclaim(task);
  }

  reload() {
    this.taskStore.loadTasks();
  }

  loadPage(page: number) {
    this.taskStore.selectPage(page);
  }

  toFieldSet(payload: any) {
    return Object.keys(payload)
      .map(prop => ({ name: prop, value: payload[prop] }));
  }
}
