import { Component, inject } from '@angular/core';
import { UserStoreService } from '../state/user.store-service';
import { NgbDropdown, NgbDropdownToggle, NgbDropdownMenu } from '@ng-bootstrap/ng-bootstrap';
import { NgFor, AsyncPipe } from '@angular/common';

@Component({
    selector: 'tasks-user-selection',
    templateUrl: './user-selection.component.html',
    styleUrls: ['./user-selection.component.scss'],
    imports: [NgbDropdown, NgbDropdownToggle, NgbDropdownMenu, NgFor, AsyncPipe]
})
export class UserSelectionComponent {
  private userStore = inject(UserStoreService);


  availableUsers$ = this.userStore.availableUsers$;
  currentProfile$ = this.userStore.currentUserProfile$;

  constructor() {
    this.userStore.loadAvailableUsers();
  }

  setCurrentUser(userIdentifier: string) {
    this.userStore.selectUser(userIdentifier);
  }
}
