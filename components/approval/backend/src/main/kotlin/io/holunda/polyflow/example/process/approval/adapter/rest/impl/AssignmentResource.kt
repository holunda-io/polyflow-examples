package io.holunda.polyflow.example.process.approval.adapter.rest.impl

import io.holunda.polyflow.example.process.approval.process.AssignmentCommand
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.adapter.rest.api.UserTaskAssignmentApiDelegate
import io.holunda.polyflow.example.process.approval.adapter.rest.model.TaskAssignmentDto
import mu.KLogging
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.CommandResultMessage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.stereotype.Component

@Component
class AssignmentResource(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
  private val commandGateway: CommandGateway
) : UserTaskAssignmentApiDelegate {

  companion object : KLogging()

  override fun submitTaskAssignmentChange(
    taskId: String,
    xCurrentUserID: String,
    taskAssignmentDto: TaskAssignmentDto
  ): ResponseEntity<Unit> {

    commandGateway.send(
      AssignmentCommand(
        taskId = taskId,
        newCandidateUsers = taskAssignmentDto.addCandidateUsers,
        newCandidateGroups = taskAssignmentDto.addCandidateGroups,
        deleteCandidateUsers = taskAssignmentDto.deleteCandidateUsers,
        deleteCandidateGroups = taskAssignmentDto.deleteCandidateGroups,
      )
    ) { _: CommandMessage<out Any>, result: CommandResultMessage<out Any> ->
      if (result.isExceptional) {
        logger.error(result.exceptionResult()) { "Error assigning groups" }
      }
    }

    /*
    requestApprovalProcessBean.changeAssignment(
      taskId = taskId,
      newCandidateUsers = taskAssignmentDto.addCandidateUsers,
      newCandidateGroups = taskAssignmentDto.addCandidateGroups,
      deleteCandidateUsers = taskAssignmentDto.deleteCandidateUsers,
      deleteCandidateGroups = taskAssignmentDto.deleteCandidateGroups,
    )
     */

    return noContent().build()
  }
}
