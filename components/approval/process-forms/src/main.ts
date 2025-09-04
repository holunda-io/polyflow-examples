import {enableProdMode, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {environment} from 'environments/environment';
import {EnvironmentHelperService} from 'app/services/environment.helper.service';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {bootstrapApplication} from '@angular/platform-browser';
import {provideAnimations} from '@angular/platform-browser/animations';
import {ApiModule} from 'process/api.module';
import {externalUrlProvider, externalUrlProviderCanActivateGuard, routes} from 'app/app-routing.module';
import {AppComponent} from 'app/app.component';
import {provideRouter} from "@angular/router";

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
          // generated server API
          ApiModule.forRoot({ rootUrl: '/example-process-approval/rest' }),
        ),
       provideBrowserGlobalErrorListeners(),
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes),
        {
          provide: externalUrlProvider,
          useValue: externalUrlProviderCanActivateGuard,
        },
        EnvironmentHelperService,
        provideHttpClient(withInterceptorsFromDi()),
        provideAnimations()
    ]
})
  .catch(err => console.error(err));

