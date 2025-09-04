import { Component, inject } from '@angular/core';
import { UserStoreService } from '../state/user.store-service';

@Component({
    selector: 'tasks-user-selection',
    templateUrl: './user-selection.component.html',
    styleUrls: ['./user-selection.component.scss'],
    standalone: false
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
