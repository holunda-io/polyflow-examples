import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {dataentryReducer} from 'app/dataentry/state/dataentry.reducer';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {UserModule} from 'app/user/user.module';

import {DataentryEffects} from 'app/dataentry/state/dataentry.effects';
import {DataentryStoreService} from 'app/dataentry/state/dataentry.store-service';

import {DataentryListComponent} from 'app/dataentry/dataentry-list/dataentry-list.component';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
    exports: [
        DataentryListComponent
    ],
    imports: [
    CommonModule,
    FormsModule,
    EffectsModule.forFeature([DataentryEffects]),
    StoreModule.forFeature('archive', dataentryReducer),
    UserModule,
    NgbModule,
    DataentryListComponent
],
    providers: [
        DataentryStoreService,
    ]
})
export class DataEntryModule {
}
