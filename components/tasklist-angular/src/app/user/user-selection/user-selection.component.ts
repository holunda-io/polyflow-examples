import { Component } from '@angular/core';
import { UserStoreService } from '../state/user.store-service';

@Component({
  selector: 'tasks-user-selection',
  templateUrl: './user-selection.component.html',
  styleUrls: ['./user-selection.component.scss']
})
export class UserSelectionComponent {

  availableUsers$ = this.userStore.availableUsers$();
  currentProfile$ = this.userStore.currentUserProfile$();

  constructor(private userStore: UserStoreService) {
    this.userStore.loadAvailableUsers();
  }

  setCurrentUser(userIdentifier: string) {
    this.userStore.selectUser({userId: userIdentifier});
  }
}
