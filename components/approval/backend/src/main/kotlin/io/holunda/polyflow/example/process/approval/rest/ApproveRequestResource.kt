package io.holunda.polyflow.example.process.approval.rest

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.rest.api.UserTaskApproveRequestApiDelegate
import io.holunda.polyflow.example.process.approval.rest.model.TaskApproveRequestFormDataDto
import io.holunda.polyflow.example.process.approval.rest.model.TaskApproveRequestSubmitDataDto
import io.holunda.polyflow.view.auth.UserService
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ApproveRequestResource(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
  private val userService: UserService
) : UserTaskApproveRequestApiDelegate {

  companion object : KLogging()

  override fun loadTaskApproveRequestFormData(
    id: String,
    xCurrentUserID: String
  ): ResponseEntity<TaskApproveRequestFormDataDto> {

    val username = userService.getUser(xCurrentUserID).username

    logger.debug { "Loading data task $id for user $username" }
    val (task, approvalRequest) = requestApprovalProcessBean.loadApproveTaskFormData(id)
    return ResponseEntity.ok(TaskApproveRequestFormDataDto(approvalRequest = approvalRequestDto(approvalRequest), task = taskDto(task)))
  }

  @Transactional
  override fun submitTaskApproveRequestSubmitData(
    id: String,
    xCurrentUserID: String,
    taskApproveRequestSubmitDataDto: TaskApproveRequestSubmitDataDto
  ): ResponseEntity<Unit> {
    val username = userService.getUser(xCurrentUserID).username

    logger.debug { "User $username is submitting data for task $id, $taskApproveRequestSubmitDataDto" }
    requestApprovalProcessBean.approveTask(id, taskApproveRequestSubmitDataDto.decision, username, taskApproveRequestSubmitDataDto.comment)
    return ResponseEntity.noContent().build()
  }
}
