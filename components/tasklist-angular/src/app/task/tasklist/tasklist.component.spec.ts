import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { FormsModule } from '@angular/forms';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import { provideMockStore } from '@ngrx/store/testing';
import { ProcessStoreService } from 'app/process/state/process.store-service';
import { SharedModule } from 'app/shared/shared.module';
import { SortableColumnComponent } from 'app/task/sorter/sortable-column.component';
import { SortDirection } from 'app/task/state/task.reducer';
import { currentUserProfile } from 'app/user/state/user.selectors';
import { UserStoreService } from 'app/user/state/user.store-service';
import { getCount, getSelectedPage, getSortingColumn, getTasks } from '../state/task.selectors';
import { TaskStoreService } from '../state/task.store-service';
import { TasklistComponent } from './tasklist.component';

describe('Component: TasklistComponent', () => {

  let component: TasklistComponent;
  let fixture: ComponentFixture<TasklistComponent>;

  beforeEach(waitForAsync(() => {

    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        SharedModule,
        NgbPagination
      ],
      declarations: [
        TasklistComponent,
        SortableColumnComponent
      ],
      providers: [
        TaskStoreService,
        UserStoreService,
        ProcessStoreService,
        provideMockStore({
          selectors: [
            { selector: getTasks, value: [] },
            { selector: getCount, value: 0 },
            { selector: getSelectedPage, value: 1 },
            { selector: getSortingColumn, value: { fieldName: 'task.dueDate', direction: SortDirection.DESC } },
            { selector: currentUserProfile, value: { username: '', userIdentifier: '', fullName: '' } },
          ]
        })
      ],
    }).compileComponents().then(() => {
      // create component and test fixture
      fixture = TestBed.createComponent(TasklistComponent);

      // get test component from the fixture
      component = fixture.componentInstance;

      // detect changes
      fixture.detectChanges();
    });
  }));

  it('should create', () => {
    expect(component).toBeDefined();
  });

});
