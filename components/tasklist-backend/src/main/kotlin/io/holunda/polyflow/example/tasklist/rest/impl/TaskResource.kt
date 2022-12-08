package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.camunda.taskpool.api.task.*
import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.ElementNotFoundException
import io.holunda.polyflow.example.tasklist.rest.api.TaskApiDelegate
import io.holunda.polyflow.example.tasklist.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.rest.model.TaskWithDataEntriesDto
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
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  private val mapper: TaskWithDataEntriesMapper
) : TaskApiDelegate {

  companion object : KLogging() {
    const val HEADER_ELEMENT_COUNT = "X-ElementCount"
  }

  override fun claim(
    id: String,
    xCurrentUserID: String?
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID ?: currentUserService.getCurrentUser())
    val task = getAuthorizedTask(id, user)

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
    id: String,
    xCurrentUserID: String?
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID ?: currentUserService.getCurrentUser())
    val task = getAuthorizedTask(id, user)

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
    id: String,
    requestBody: Map<String, Any>,
    xCurrentUserID: String?
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID ?: currentUserService.getCurrentUser())
    val task = getAuthorizedTask(id, user)

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
    id: String,
    body: OffsetDateTime,
    xCurrentUserID: String?
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID ?: currentUserService.getCurrentUser())
    val task = getAuthorizedTask(id, user)

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
    id: String,
    xCurrentUserID: String?
  ): ResponseEntity<Unit> {

    val user = userService.getUser(xCurrentUserID ?: currentUserService.getCurrentUser())
    val task = getAuthorizedTask(id, user)

    taskServiceGateway.send(
      UndeferInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey
      )
    )

    return ResponseEntity.noContent().build()
  }

  override fun getTasks(
    page: Int,
    size: Int,
    sort: String?,
    filters: List<String>,
    xCurrentUserID: String?
  ): ResponseEntity<List<TaskWithDataEntriesDto>> {

    val userIdentifier = xCurrentUserID ?: currentUserService.getCurrentUser()
    val user = userService.getUser(userIdentifier)

    val result = taskServiceGateway.getTasks(user, page, sort, size, filters)

    val responseHeaders = HttpHeaders().apply {
      this[HEADER_ELEMENT_COUNT] = result.totalElementCount.toString()
    }

    return ResponseEntity.ok()
      .headers(responseHeaders)
      .body(result.elements.map { mapper.dto(it) })
  }

  private fun getAuthorizedTask(id: String, user: User): Task = taskServiceGateway.getTask(id)
    .apply {
      if (!isAuthorized(this, user)) {
        // if the user is not allowed to access, behave if the task is not found
        throw ElementNotFoundException()
      }
    }


  private fun isAuthorized(task: Task, user: User) =
    task.assignee == user.username || task.candidateUsers.contains(user.username) || task.candidateGroups.any { requiredGroup ->
      user.groups.contains(
        requiredGroup
      )
    }
}

