package io.holunda.polyflow.example.process.approval.rest

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.rest.api.ChangeAssignmentApi
import io.holunda.polyflow.example.process.approval.rest.model.TaskAssignmentDto
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping(path = [Rest.REST_PREFIX])
class TaskAssignmentController(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
) : ChangeAssignmentApi {

  override fun submitTaskAssignmentChange(
    @PathVariable("taskId") taskId: String,
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String,
    @Valid @RequestBody taskAssignmentDto: TaskAssignmentDto
  ): ResponseEntity<Void> {
    requestApprovalProcessBean.changeAssignment(
      taskId = taskId,
      newCandidateUsers = taskAssignmentDto.addCandidateUsers,
      newCandidateGroups = taskAssignmentDto.addCandidateGroups,
      deleteCandidateUsers = taskAssignmentDto.deleteCandidateUsers,
      deleteCandidateGroups = taskAssignmentDto.deleteCandidateGroups,
    )
    return noContent().build()
  }
}