package io.holunda.polyflow.example.process.approval.rest

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.rest.api.AmendRequestApi
import io.holunda.polyflow.example.process.approval.rest.api.UserTaskAmendRequestApiDelegate
import io.holunda.polyflow.example.process.approval.rest.model.TaskAmendRequestFormDataDto
import io.holunda.polyflow.example.process.approval.rest.model.TaskAmendRequestSubmitDataDto
import io.holunda.polyflow.view.auth.UserService
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping(path = [Rest.REST_PREFIX])
class AmendRequestTaskController(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
  private val userService: UserService
) : UserTaskAmendRequestApiDelegate {

  companion object : KLogging()

  override fun loadTaskAmendRequestFormData(
    @PathVariable("id") id: String,
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String
  ): ResponseEntity<TaskAmendRequestFormDataDto> {

    val username = userService.getUser(xCurrentUserID).username

    logger.debug { "Loading data task $id for user $username" }

    val (task, approvalRequest) = requestApprovalProcessBean.loadAmendTaskFormData(id)
    return ResponseEntity.ok(TaskAmendRequestFormDataDto().approvalRequest(approvalRequestDto(approvalRequest)).task(taskDto(task)))
  }

  @Transactional
  override fun submitTaskAmendRequestSubmitData(
    @PathVariable("id") id: String,
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String,
    @Valid @RequestBody payload: TaskAmendRequestSubmitDataDto
  ): ResponseEntity<Void> {

    val username = userService.getUser(xCurrentUserID).username
    logger.debug { "User $username is submitting data for task $id, $payload" }

    requestApprovalProcessBean.amendTask(
      taskId = id,
      action = payload.action,
      request = request(payload.approvalRequest),
      username = username,
      comment = payload.comment
    )
    return ResponseEntity.noContent().build()
  }
}
