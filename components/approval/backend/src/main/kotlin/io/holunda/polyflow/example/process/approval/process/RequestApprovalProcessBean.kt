package io.holunda.polyflow.example.process.approval.process

import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Values.RESUBMIT
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Variables.AMEND_ACTION
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Variables.APPROVE_DECISION
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Variables.COMMENT
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Variables.ORIGINATOR
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Variables.PROJECTION_REVISION
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcess.Variables.REQUEST_ID
import io.holunda.polyflow.example.process.approval.service.Request
import io.holunda.polyflow.example.process.approval.service.RequestService
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.camunda.bpm.engine.task.Task
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.NoSuchElementException

@Component
@Transactional
class RequestApprovalProcessBean(
  private val runtimeService: RuntimeService,
  private val taskService: TaskService,
  private val requestService: RequestService
) {

  /**
   * Creates draft.
   * @param request request draft to save.
   * @param originator user saved the request.
   * @param revision revision of te command.
   */
  fun submitDraft(request: Request, originator: String, revision: Long = 1L) {
    val lastRevision = requestService.addRequest(request, originator, revision)
    startProcess(request.id, originator, lastRevision)
  }

  /**
   * Starts the process for a given request id.
   */
  fun startProcess(requestId: String, originator: String, revision: Long = 1L): String {
    runtimeService.startProcessInstanceByKey(RequestApprovalProcess.KEY,
      requestId,
      builder()
        .set(REQUEST_ID, requestId)
        .set(ORIGINATOR, originator)
        .set(PROJECTION_REVISION, revision)
        .build()
    )
    return requestId
  }

  /**
   * Completes the approval request task with given id, decision and optional comment.
   */
  fun approveTask(taskId: String, decision: String, username: String, comment: String?) {
    if (!RequestApprovalProcess.Values.APPROVE_DECISION_VALUES.contains(decision.uppercase())) {
      throw IllegalArgumentException("Only one of APPROVE, RETURN, REJECT is supported.")
    }

    val task = taskService
      .createTaskQuery()
      .taskId(taskId)
      .taskDefinitionKey(RequestApprovalProcess.Elements.APPROVE_REQUEST)
      .singleResult() ?: throw NoSuchElementException("Task with id $taskId not found.")

    taskService.claim(task.id, username)

    taskService.complete(task.id,
      builder()
        .set(APPROVE_DECISION, decision.uppercase(Locale.getDefault()))
        .set(COMMENT, comment)
        .build()
    )
  }

  /**
   * Completes the amend request task with given id, action and optional comment.
   */
  fun amendTask(taskId: String, action: String, request: Request, username: String, comment: String?) {
    if (!RequestApprovalProcess.Values.AMEND_ACTION_VALUES.contains(action.uppercase())) {
      throw IllegalArgumentException("Only one of CANCEL, RESUBMIT is supported.")
    }

    val task = taskService
      .createTaskQuery()
      .taskId(taskId)
      .taskDefinitionKey(RequestApprovalProcess.Elements.AMEND_REQUEST)
      .singleResult() ?: throw NoSuchElementException("Task with id $taskId not found.")

    val revision = PROJECTION_REVISION.from(taskService, task.id).get() + 1

    if (task.assignee != null) {
      // un-claim the task
      if (task.assignee != username) {
        task.assignee = null
        taskService.saveTask(task)
      }
      taskService.claim(task.id, username)
    }

    val variables = builder()
      .set(AMEND_ACTION, action.uppercase(Locale.getDefault()))
      .set(COMMENT, comment)


    if (action == RESUBMIT) {
      val updatedRevision = requestService.updateRequest(id = request.id, request = request, username = username, revision = revision)
      variables
        .set(PROJECTION_REVISION, updatedRevision)
    }
    taskService.complete(task.id, variables.build())
  }

  /**
   * Deletes all instances.
   */
  fun deleteAllInstances() {
    getAllInstancesQuery()
      .list()
      .forEach {
        runtimeService
          .deleteProcessInstance(it.processInstanceId, "Deleted by the mass deletion REST call")
      }
  }

  /**
   * Retrieve the number of running instances.
   */
  fun countInstances() = getAllInstancesQuery().active().count()

  /**
   * Loads approve task form data.
   */
  fun loadApproveTaskFormData(id: String): TaskAndRequest {
    return loadTaskAndRequest(id, RequestApprovalProcess.Elements.APPROVE_REQUEST)
  }

  /**
   * Loads amend task form data.
   */
  fun loadAmendTaskFormData(id: String): TaskAndRequest {
    return loadTaskAndRequest(id, RequestApprovalProcess.Elements.AMEND_REQUEST)
  }

  private fun loadTaskAndRequest(taskId: String, definitionKey: String): TaskAndRequest {
    val task = taskService.createTaskQuery()
      .taskId(taskId)
      .taskDefinitionKey(definitionKey)
      .initializeFormKeys()
      .singleResult() ?: throw NoSuchElementException("Task with id $taskId not found.")

    val requestId = REQUEST_ID.from(runtimeService, task.executionId).optional.orElseThrow { NoSuchElementException("Request id could not be found for task $taskId") }
    val revision = PROJECTION_REVISION.from(runtimeService, task.executionId).optional.orElseThrow { NoSuchElementException("Project revision could not be found for task $taskId") }

    val request = this.requestService.getRequest(requestId, revision)
    return TaskAndRequest(task = task, approvalRequest = request)
  }

  /**
   * Retrieves all running instances.
   */
  private fun getAllInstancesQuery() =
    runtimeService
      .createProcessInstanceQuery()
      .processDefinitionKey(RequestApprovalProcess.KEY)

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  fun changeAssignment(taskId: String,
                       newCandidateUsers: List<String>,
                       newCandidateGroups: List<String>,
                       deleteCandidateUsers: List<String>,
                       deleteCandidateGroups: List<String>,
  ) {
    newCandidateUsers.forEach { user -> taskService.addCandidateUser(taskId, user) }
    newCandidateGroups.forEach { group -> taskService.addCandidateGroup(taskId, group) }
    deleteCandidateUsers.forEach { user -> taskService.deleteCandidateUser(taskId, user) }
    deleteCandidateGroups.forEach { group -> taskService.deleteCandidateGroup(taskId, group) }
  }

}

data class TaskAndRequest(val task: Task, val approvalRequest: Request)
