import {InjectionToken} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Routes} from '@angular/router';
import {PageNotFoundComponent} from 'app/components/page-not-found/page-not-found.component';
import {TasklistComponent} from 'app/task/tasklist/tasklist.component';
import {DataentryListComponent} from 'app/dataentry/dataentry-list/dataentry-list.component';

export const externalUrlProvider = new InjectionToken('externalUrlRedirectResolver');

export const routes: Routes = [
  {
    path: 'externalRedirect',
    canActivate: [externalUrlProvider],
    // We need a component here because we cannot define the route otherwise
    component: PageNotFoundComponent,
  },
  { path: 'tasks', component: TasklistComponent },
  { path: 'archive', component: DataentryListComponent },
  { path: '', redirectTo: 'tasks', pathMatch: 'full'}
];

export const externalUrlProviderActivateGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {

  const externalUrl = route.paramMap.get('externalUrl');
  window.open(externalUrl, '_self');
  return false;
};
