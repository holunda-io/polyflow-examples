import { Component, inject } from '@angular/core';
import { itemsPerPage } from 'app/task/state/task.selectors';
import { TaskStoreService } from 'app/task/state/task.store-service';
import { UserStoreService } from 'app/user/state/user.store-service';
import { Task } from 'tasklist/models';
import { SortableColumnComponent } from '../sorter/sortable-column.component';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf, AsyncPipe, DatePipe } from '@angular/common';
import { ExternalUrlDirective } from 'app/shared/external-url.directive';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import { FieldNamePipe } from 'app/shared/field-name.pipe';

@Component({
    selector: 'tasks-tasklist',
    templateUrl: './tasklist.component.html',
    styleUrls: ['tasklist.component.scss'],
    imports: [SortableColumnComponent, FormsModule, NgFor, ExternalUrlDirective, NgIf, NgbPagination, AsyncPipe, DatePipe, FieldNamePipe]
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

  toFieldSet(payload: Record<string, unknown>) {
    return Object.keys(payload)
      .map(prop => ({ name: prop, value: payload[prop] }));
  }
}
