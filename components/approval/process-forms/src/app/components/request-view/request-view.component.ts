import { Component, Input } from '@angular/core';
import { ApprovalRequestDraft } from 'process/models/approval-request-draft';

@Component({
    selector: 'app-request-view',
    templateUrl: './request-view.component.html',
    styleUrls: [],
    standalone: false
})
export class RequestViewComponent {

  @Input()
  approvalRequest: ApprovalRequestDraft;

}
