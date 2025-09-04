import { Component, OnInit, inject } from '@angular/core';
import {UserStoreService} from 'app/user/state/user.store-service';
import { HeaderComponent } from './header/header.component';
import { RouterLinkActive, RouterLink, RouterOutlet } from '@angular/router';
import { ProcesslistComponent } from './process/process-list/process-list.component';
import { SearchComponent } from './search/search.component';
import { UserSelectionComponent } from './user/user-selection/user-selection.component';
import { FooterComponent } from './footer/footer.component';

@Component({
    selector: 'tasks-root',
    templateUrl: './app.component.html',
    styleUrls: ['app.component.scss'],
    imports: [HeaderComponent, RouterLinkActive, RouterLink, ProcesslistComponent, SearchComponent, UserSelectionComponent, RouterOutlet, FooterComponent]
})
export class AppComponent implements OnInit {
  private userStore = inject(UserStoreService);


  ngOnInit(): void {
    this.userStore.loadInitialUser();
  }
}
