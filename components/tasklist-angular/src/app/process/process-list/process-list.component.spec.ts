import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { FormsModule } from '@angular/forms';
import { provideMockStore } from '@ngrx/store/testing';
import { startableProcesses } from '../state/process.selectors';
import { ProcessStoreService } from '../state/process.store-service';
import { ProcesslistComponent } from './process-list.component';

describe('Component: TasklistComponent', () => {

  let component: ProcesslistComponent;
  let fixture: ComponentFixture<ProcesslistComponent>;

  beforeEach(waitForAsync(() => {

    TestBed.configureTestingModule({
      imports: [
        FormsModule
      ],
      declarations: [
        ProcesslistComponent
      ],
      providers: [
        ProcessStoreService,
        provideMockStore({
          selectors: [
            { selector: startableProcesses, value: [] }
          ]
        })
      ],
    }).compileComponents().then(() => {
      // create component and test fixture
      fixture = TestBed.createComponent(ProcesslistComponent);

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
