import { Component } from '@angular/core';

@Component({
    selector: 'tasks-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
    standalone: false
})
export class HeaderComponent {

  isNavbarCollapsed = false;

}
