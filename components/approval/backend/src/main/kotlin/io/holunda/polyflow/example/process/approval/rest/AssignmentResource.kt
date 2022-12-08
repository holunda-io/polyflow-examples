package io.holunda.polyflow.example.process.approval.rest

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.rest.api.UserTaskAssignmentApiDelegate
import io.holunda.polyflow.example.process.approval.rest.model.TaskAssignmentDto
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.stereotype.Component

@Component
class AssignmentResource(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
) : UserTaskAssignmentApiDelegate {

  override fun submitTaskAssignmentChange(
    taskId: String,
    xCurrentUserID: String,
    taskAssignmentDto: TaskAssignmentDto
  ): ResponseEntity<Unit> {
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
