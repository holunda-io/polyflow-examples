import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {provideMockStore} from '@ngrx/store/testing';
import {DataentryListComponent} from 'app/dataentry/dataentry-list/dataentry-list.component';
import {dataEntries} from '../state/dataentry.selectors';
import {DataentryStoreService} from '../state/dataentry.store-service';
import {TaskStoreService} from "app/task/state/task.store-service";
import {getSortingColumn} from "app/task/state/task.selectors";
import {SortDirection} from "app/task/state/task.reducer";

describe('Component: DataentrylistComponent', () => {

  let component: DataentryListComponent;
  let fixture: ComponentFixture<DataentryListComponent>;

  beforeEach(waitForAsync(async () => {

    await TestBed.configureTestingModule({
    imports: [
        DataentryListComponent,
    ],
    providers: [
        DataentryStoreService,
        TaskStoreService,
        provideMockStore({
            selectors: [
                { selector: dataEntries, value: [] },
                {selector: getSortingColumn, value: {fieldName: 'task.dueDate', direction: SortDirection.DESC}}
            ]
        }),
    ],
}).compileComponents().then(() => {
      // create component and test fixture
      fixture = TestBed.createComponent(DataentryListComponent);

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
