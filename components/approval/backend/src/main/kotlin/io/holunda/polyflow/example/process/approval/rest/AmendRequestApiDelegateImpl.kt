package io.holunda.polyflow.example.process.approval.rest

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.rest.api.UserTaskAmendRequestApiDelegate
import io.holunda.polyflow.example.process.approval.rest.model.TaskAmendRequestFormDataDto
import io.holunda.polyflow.example.process.approval.rest.model.TaskAmendRequestSubmitDataDto
import io.holunda.polyflow.view.auth.UserService
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import javax.validation.Valid

@Component
class AmendRequestApiDelegateImpl(
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
    return ResponseEntity.ok(TaskAmendRequestFormDataDto(task = taskDto(task), approvalRequest =approvalRequestDto(approvalRequest)))
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
