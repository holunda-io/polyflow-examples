import {enableProdMode, importProvidersFrom, isDevMode, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';

import {environment} from 'environments/environment';
import {ApiConfiguration} from 'tasklist/api-configuration';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {bootstrapApplication} from '@angular/platform-browser';
import {provideAnimations} from '@angular/platform-browser/animations';
import {externalUrlProvider, externalUrlProviderActivateGuard, routes} from 'app/app-routing.module';
import {ActionReducer, provideState, provideStore} from '@ngrx/store';
import {storePersist} from 'app/store-persist';
import {provideEffects} from '@ngrx/effects';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {ApiModule} from 'tasklist/api.module';
import {AppComponent} from 'app/app.component';
import {DataentryStoreService} from "app/dataentry/state/dataentry.store-service";
import {DataentryEffects} from "app/dataentry/state/dataentry.effects";
import {dataentryReducer} from "app/dataentry/state/dataentry.reducer";
import {TaskStoreService} from "app/task/state/task.store-service";
import {TaskEffects} from "app/task/state/task.effects";
import {taskReducer} from "app/task/state/task.reducer";
import {UserStoreService} from "app/user/state/user.store-service";
import {UserEffects} from "app/user/state/user.effects";
import {userReducer} from "app/user/state/user.reducer";
import {ProcessStoreService} from "app/process/state/process.store-service";
import {ProcessEffects} from "app/process/state/process.effects";
import {processReducer} from "app/process/state/process.reducer";
import {provideRouter} from "@angular/router";

if (environment.production) {
  enableProdMode();
}

/**
 * Helper for debugging dispatched actions during java-based integration tests
 * */
const logActions = (reducer: ActionReducer<unknown>): ActionReducer<unknown> => {
  return (state, action) => {
    console.log(action)
    return reducer(state, action);
  }
}

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
          ApiModule,
        ),
      provideBrowserGlobalErrorListeners(),
      provideZoneChangeDetection({ eventCoalescing: true }),
      // routing
      provideRouter(routes),
      {
        provide: externalUrlProvider,
        useValue: externalUrlProviderActivateGuard,
      },
      // http client
      { provide: ApiConfiguration, useValue: { rootUrl: '/polyflow-platform/rest' } },
      provideHttpClient(withInterceptorsFromDi()),
      // ngrx store
      provideStore({}, {
        metaReducers: [storePersist, logActions],
        runtimeChecks: { strictStateImmutability: true, strictActionImmutability: true }
      }),
      provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
      provideAnimations(),
      // Dataentry store
      DataentryStoreService,
      provideEffects(DataentryEffects),
      provideState('archive', dataentryReducer),
      // task store
      TaskStoreService,
      provideEffects(TaskEffects),
      provideState('task', taskReducer),
      // user store
      UserStoreService,
      provideEffects(UserEffects),
      provideState('user', userReducer),
      // processs store
      ProcessStoreService,
      provideEffects(ProcessEffects),
      provideState('process', processReducer)
    ]
})
  .catch(err => console.error(err));

