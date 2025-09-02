import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StoreModule} from '@ngrx/store';
import {taskReducer} from 'app/task/state/task.reducer';
import {EffectsModule} from '@ngrx/effects';
import {TaskEffects} from 'app/task/state/task.effects';
import {UserModule} from 'app/user/user.module';
import {TasklistComponent} from 'app/task/tasklist/tasklist.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SortableColumnComponent} from 'app/task/sorter/sortable-column.component';
import {FormsModule} from '@angular/forms';
import {TaskStoreService} from 'app/task/state/task.store-service';


@NgModule({
    exports: [TasklistComponent],
    imports: [
    CommonModule,
    FormsModule,
    StoreModule.forFeature('task', taskReducer),
    EffectsModule.forFeature([TaskEffects]),
    UserModule,
    NgbModule,
    TasklistComponent,
    SortableColumnComponent
],
    providers: [
        TaskStoreService
    ]
})
export class TaskModule { }
