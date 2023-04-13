package io.holunda.polyflow.example.process.approval.adapter.rest.impl

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.adapter.rest.api.UserTaskAmendRequestApiDelegate
import io.holunda.polyflow.example.process.approval.adapter.rest.approvalRequestDto
import io.holunda.polyflow.example.process.approval.adapter.rest.model.TaskAmendRequestFormDataDto
import io.holunda.polyflow.example.process.approval.adapter.rest.model.TaskAmendRequestSubmitDataDto
import io.holunda.polyflow.example.process.approval.adapter.rest.request
import io.holunda.polyflow.example.process.approval.adapter.rest.taskDto
import io.holunda.polyflow.view.auth.UserService
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AmendRequestResource(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
  private val userService: UserService
) : UserTaskAmendRequestApiDelegate {

  companion object : KLogging()

  override fun loadTaskAmendRequestFormData(
    id: String,
    xCurrentUserID: String
  ): ResponseEntity<TaskAmendRequestFormDataDto> {

    val username = userService.getUser(xCurrentUserID).username

    logger.debug { "Loading data task $id for user $username" }

    val (task, approvalRequest) = requestApprovalProcessBean.loadAmendTaskFormData(id)
    return ResponseEntity.ok(TaskAmendRequestFormDataDto(task = taskDto(task), approvalRequest = approvalRequestDto(approvalRequest)))
  }

  @Transactional
  override fun submitTaskAmendRequestSubmitData(
    id: String,
    xCurrentUserID: String,
    taskAmendRequestSubmitDataDto: TaskAmendRequestSubmitDataDto
  ): ResponseEntity<Unit> {

    val username = userService.getUser(xCurrentUserID).username
    logger.debug { "User $username is submitting data for task $id, $taskAmendRequestSubmitDataDto" }

    requestApprovalProcessBean.amendTask(
      taskId = id,
      action = taskAmendRequestSubmitDataDto.action,
      request = request(requireNotNull(taskAmendRequestSubmitDataDto.approvalRequest) { "Approval request is mandatory, but it was null." }),
      username = username,
      comment = taskAmendRequestSubmitDataDto.comment
    )
    return ResponseEntity.noContent().build()
  }

}
