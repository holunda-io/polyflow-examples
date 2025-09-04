import {enableProdMode, importProvidersFrom} from '@angular/core';
import {environment} from 'environments/environment';
import {EnvironmentHelperService} from 'app/services/environment.helper.service';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {bootstrapApplication, BrowserModule} from '@angular/platform-browser';
import {provideAnimations} from '@angular/platform-browser/animations';
import {ApiModule} from 'process/api.module';
import {AppRoutingModule} from 'app/app-routing.module';
import {AppComponent} from 'app/app.component';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
          BrowserModule,
          // generated server API
          ApiModule.forRoot({ rootUrl: '/example-process-approval/rest' }),
          // routing
          AppRoutingModule
        ),
        EnvironmentHelperService,
        provideHttpClient(withInterceptorsFromDi()),
        provideAnimations()
    ]
})
  .catch(err => console.error(err));

