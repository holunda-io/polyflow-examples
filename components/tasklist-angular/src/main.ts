import { enableProdMode, isDevMode, importProvidersFrom } from '@angular/core';

import { environment } from './environments/environment';
import { ApiConfiguration } from 'tasklist/api-configuration';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app/app-routing.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { StoreModule } from '@ngrx/store';
import { storePersist } from 'app/store-persist';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { ApiModule } from 'tasklist/api.module';
import { UserModule } from 'app/user/user.module';
import { ProcessModule } from 'app/process/process.module';
import { DataEntryModule } from 'app/dataentry/dataentry.module';
import { TaskModule } from 'app/task/task.module';
import { AppComponent } from './app/app.component';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
          BrowserModule,
          FormsModule,
          AppRoutingModule,
          NgbModule,
          StoreModule.forRoot({}, {
            metaReducers: [storePersist],
            runtimeChecks: { strictStateImmutability: true, strictActionImmutability: true }
        }),
          EffectsModule.forRoot([]),
          StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: !isDevMode() }),
          // generated
          ApiModule,
          // own
          UserModule,
          ProcessModule,
          DataEntryModule,
          TaskModule
        ),
        { provide: ApiConfiguration, useValue: { rootUrl: '/polyflow-platform/rest' } },
        provideHttpClient(withInterceptorsFromDi()),
        provideAnimations()
    ]
})
  .catch(err => console.error(err));

