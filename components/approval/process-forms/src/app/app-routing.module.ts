import {InjectionToken} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Routes} from '@angular/router';
import {ApproveTaskComponent} from 'app/tasks/approve-request/approve-task.component';
import {AmendTaskComponent} from 'app/tasks/amend-request/amend-task.component';
import {PageNotFoundComponent} from 'app/tasks/page-not-found/page-not-found.component';
import {StartComponent} from 'app/tasks/start/start.component';
import {ApprovalRequestComponent} from 'app/data/approval-request/approval-request.component';

export const externalUrlProvider = new InjectionToken('externalUrlRedirectResolver');

export const routes: Routes = [
  {
    path: 'start',
    component: StartComponent,
  },
  {
    path: 'approval-request/:requestId',
    component: ApprovalRequestComponent
  },
  {
    path: 'tasks/amend-request/:taskId',
    component: AmendTaskComponent,
  },
  {
    path: 'tasks/approve-request/:taskId',
    component: ApproveTaskComponent,
  },
  {
    path: 'externalRedirect',
    canActivate: [externalUrlProvider],
    // We need a component here because we cannot define the route otherwise
    component: PageNotFoundComponent,
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

export const externalUrlProviderCanActivateGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const externalUrl = route.paramMap.get('externalUrl');
  window.open(externalUrl, '_self');
  return false;
};
