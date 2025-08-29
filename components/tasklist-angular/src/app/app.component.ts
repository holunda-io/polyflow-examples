import { Component, OnInit, inject } from '@angular/core';
import {UserStoreService} from 'app/user/state/user.store-service';

@Component({
    selector: 'tasks-root',
    templateUrl: './app.component.html',
    styleUrls: ['app.component.scss'],
    standalone: false
})
export class AppComponent implements OnInit {
  private userStore = inject(UserStoreService);


  ngOnInit(): void {
    this.userStore.loadInitialUser();
  }
}
