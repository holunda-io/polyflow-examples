import { BrowserModule } from '@angular/platform-browser';
import { isDevMode, NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import { FormsModule } from '@angular/forms';
import localeFr from '@angular/common/locales/fr';
import localeDe from '@angular/common/locales/de';
import localeEn from '@angular/common/locales/en';

import { ApiModule } from 'tasklist/api.module';
import { AppComponent } from 'app/app.component';
import { PageNotFoundComponent } from 'app/components/page-not-found/page-not-found.component';
import { AppRoutingModule } from './app-routing.module';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { SearchComponent } from './search/search.component';

import { UserModule } from 'app/user/user.module';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { ProcessModule } from 'app/process/process.module';
import { SharedModule } from 'app/shared/shared.module';
import { DataEntryModule } from 'app/dataentry/dataentry.module';
import { TaskModule } from 'app/task/task.module';
import { storePersist } from 'app/store-persist';
import { HttpClientModule } from '@angular/common/http';
import { ApiConfiguration } from 'tasklist/api-configuration';
import { StoreDevtoolsModule } from "@ngrx/store-devtools";


registerLocaleData(localeFr, 'fr');
registerLocaleData(localeDe, 'de');
registerLocaleData(localeEn, 'en');


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    FooterComponent,
    HeaderComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    NgbModule,
    StoreModule.forRoot({}, {
      metaReducers: [storePersist],
      runtimeChecks: {strictStateImmutability: true, strictActionImmutability: true}
    }),
    EffectsModule.forRoot([]),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: !isDevMode()}),
    // generated
    ApiModule,
    // own
    UserModule,
    ProcessModule,
    DataEntryModule,
    TaskModule,
    SharedModule
  ],
  bootstrap: [AppComponent],
  providers: [
    {provide: ApiConfiguration, useValue: {rootUrl: '/polyflow-platform/rest'}}
  ]
})
export class AppModule {
}
