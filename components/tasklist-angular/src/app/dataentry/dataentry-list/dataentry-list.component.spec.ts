import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { FormsModule } from '@angular/forms';
import { DataentryListComponent } from 'app/dataentry/dataentry-list/dataentry-list.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'app/shared/shared.module';
import { dataEntries } from '../state/dataentry.selectors';
import { DataentryStoreService } from '../state/dataentry.store-service';

describe('Component: DataentrylistComponent', () => {

  let component: DataentryListComponent;
  let fixture: ComponentFixture<DataentryListComponent>;

  beforeEach(waitForAsync(() => {

    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        SharedModule,
        NgbModule,
      ],
      declarations: [
        DataentryListComponent
      ],
      providers: [
        DataentryStoreService,
        provideMockStore({
          selectors: [
            { selector: dataEntries, value: [] }
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
