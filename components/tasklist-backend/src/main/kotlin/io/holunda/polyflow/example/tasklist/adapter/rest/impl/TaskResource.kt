package io.holunda.polyflow.example.tasklist.adapter.rest.impl

import io.holunda.camunda.taskpool.api.task.*
import io.holunda.polyflow.example.tasklist.adapter.rest.ElementNotFoundException
import io.holunda.polyflow.example.tasklist.adapter.rest.api.TaskApiDelegate
import io.holunda.polyflow.example.tasklist.adapter.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.adapter.rest.model.TaskWithDataEntriesDto
import io.holunda.polyflow.view.Task
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.auth.UserService
import mu.KLogging
import org.camunda.bpm.engine.variable.Variables
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*

@Component
class TaskResource(
  private val taskServiceGateway: TaskServiceGateway,
  private val userService: UserService,
  private val mapper: TaskWithDataEntriesMapper
) : TaskApiDelegate {

  companion object : KLogging() {
    const val HEADER_ELEMENT_COUNT = "X-ElementCount"
  }

  override fun getTasks(
    xCurrentUserID: String,
    page: Int,
    size: Int,
    sort: String?,
    filters: List<String>?,
  ): ResponseEntity<List<TaskWithDataEntriesDto>> {

    val user = userService.getUser(xCurrentUserID)
    val result = taskServiceGateway.getTasks(user, page, sort, size, filters ?: listOf())

    return ResponseEntity
      .ok()
      .headers(HttpHeaders().apply { this[HEADER_ELEMENT_COUNT] = result.totalElementCount.toString() })
      .body(result.elements.map { mapper.dto(it) })
  }


  override fun claim(
    taskId: String,
    xCurrentUserID: String
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID)
    val task = getAuthorizedTask(taskId, user)

    taskServiceGateway.send(
      ClaimInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey,
        assignee = user.username
      )
    )

    return ResponseEntity.noContent().build()
  }

  override fun unclaim(
    taskId: String,
    xCurrentUserID: String
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID)
    val task = getAuthorizedTask(taskId, user)

    taskServiceGateway.send(
      UnclaimInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey
      )
    )

    return ResponseEntity.noContent().build()
  }

  override fun complete(
    taskId: String,
    xCurrentUserID: String,
    requestBody: Map<String, Any>
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID)
    val task = getAuthorizedTask(taskId, user)

    taskServiceGateway.send(
      CompleteInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey,
        payload = Variables.createVariables().apply { putAll(requestBody) },
        assignee = user.username
      )
    )

    return ResponseEntity.noContent().build()
  }


  override fun defer(
    taskId: String,
    xCurrentUserID: String,
    body: OffsetDateTime
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID)
    val task = getAuthorizedTask(taskId, user)

    taskServiceGateway.send(
      DeferInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey,
        followUpDate = Date.from(body.toInstant())
      )
    )

    return ResponseEntity.noContent().build()
  }

  override fun undefer(
    taskId: String,
    xCurrentUserID: String
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID)
    val task = getAuthorizedTask(taskId, user)

    taskServiceGateway.send(
      UndeferInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey
      )
    )

    return ResponseEntity.noContent().build()
  }

  private fun getAuthorizedTask(taskId: String, user: User): Task = taskServiceGateway
    .getTask(taskId)
    .apply {
      if (!isAuthorized(this, user)) {
        // if the user is not allowed to access, behave if the task is not found
        throw ElementNotFoundException()
      }
    }

  private fun isAuthorized(task: Task, user: User) =
    task.assignee == user.username
      || task.candidateUsers.contains(user.username)
      || task.candidateGroups.any { requiredGroup -> user.groups.contains(requiredGroup) }
}

