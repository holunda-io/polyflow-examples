import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FieldNamePipe} from 'app/shared/field-name.pipe';
import {ExternalUrlDirective} from 'app/shared/external-url.directive';

@NgModule({
    exports: [
        FieldNamePipe,
        ExternalUrlDirective
    ],
    imports: [
        CommonModule,
        FieldNamePipe,
        ExternalUrlDirective,
    ],
    providers: []
})
export class SharedModule {
}
