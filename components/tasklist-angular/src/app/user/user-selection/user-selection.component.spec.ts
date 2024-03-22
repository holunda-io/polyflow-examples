import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { provideMockStore } from '@ngrx/store/testing';
import { availableUsers, currentUserProfile } from '../state/user.selectors';
import { UserStoreService } from '../state/user.store-service';
import { UserSelectionComponent } from './user-selection.component';

describe('UserSelectionComponent', () => {
  let component: UserSelectionComponent;
  let fixture: ComponentFixture<UserSelectionComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [UserSelectionComponent],
      providers: [
        UserStoreService,
        provideMockStore({
          selectors: [
            { selector: currentUserProfile, value: { username: '', userIdentifier: '', fullName: '' } },
            { selector: availableUsers, value: [] }
          ]
        })
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
