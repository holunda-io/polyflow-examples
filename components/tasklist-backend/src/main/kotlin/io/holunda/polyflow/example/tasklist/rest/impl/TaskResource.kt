package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.camunda.taskpool.api.task.*
import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.ElementNotFoundException
import io.holunda.polyflow.example.tasklist.rest.Rest
import io.holunda.polyflow.example.tasklist.rest.api.TaskApi
import io.holunda.polyflow.example.tasklist.rest.impl.UserProfileResource.Companion.HEADER_CURRENT_USER
import io.holunda.polyflow.example.tasklist.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.rest.model.TaskWithDataEntriesDto
import io.holunda.polyflow.view.Task
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.auth.UserService
import mu.KLogging
import org.camunda.bpm.engine.variable.Variables
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime
import java.util.*

@RestController
@CrossOrigin
@RequestMapping(Rest.REQUEST_PATH)
class TaskResource(
  private val taskServiceGateway: TaskServiceGateway,
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  private val mapper: TaskWithDataEntriesMapper
) : TaskApi {

  companion object : KLogging() {
    const val HEADER_ELEMENT_COUNT = "X-ElementCount"
  }

  override fun claim(
    id: String,
    xCurrentUserID: Optional<String>
  ): ResponseEntity<Void> {

    val user = userService.getUser(xCurrentUserID.orElseGet { currentUserService.getCurrentUser() })
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
    xCurrentUserID: Optional<String>
  ): ResponseEntity<Void> {

    val user = userService.getUser(xCurrentUserID.orElseGet { currentUserService.getCurrentUser() })
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
    xCurrentUserID: Optional<String>,
    payload: Map<String, Any>
  ): ResponseEntity<Void> {

    val user = userService.getUser(xCurrentUserID.orElseGet { currentUserService.getCurrentUser() })
    val task = getAuthorizedTask(id, user)

    taskServiceGateway.send(
      CompleteInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey,
        payload = Variables.createVariables().apply { putAll(payload) },
        assignee = user.username
      )
    )

    return ResponseEntity.noContent().build()
  }


  override fun defer(
    id: String,
    xCurrentUserID: Optional<String>,
    followUpDate: OffsetDateTime
  ): ResponseEntity<Void> {

    val user = userService.getUser(xCurrentUserID.orElseGet { currentUserService.getCurrentUser() })
    val task = getAuthorizedTask(id, user)

    taskServiceGateway.send(
      DeferInteractionTaskCommand(
        id = task.id,
        sourceReference = task.sourceReference,
        taskDefinitionKey = task.taskDefinitionKey,
        followUpDate = Date.from(followUpDate.toInstant())
      )
    )

    return ResponseEntity.noContent().build()
  }

  override fun undefer(
    id: String,
    xCurrentUserID: Optional<String>
  ): ResponseEntity<Void> {

    val user = userService.getUser(xCurrentUserID.orElseGet { currentUserService.getCurrentUser() })
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
    @RequestParam(value = "page") page: Optional<Int>,
    @RequestParam(value = "size") size: Optional<Int>,
    @RequestParam(value = "sort") sort: Optional<String>,
    @RequestParam(value = "filter") filters: Optional<List<String>>,
    @RequestHeader(value = HEADER_CURRENT_USER, required = false) xCurrentUserID: Optional<String>
  ): ResponseEntity<List<TaskWithDataEntriesDto>> {

    val userIdentifier = xCurrentUserID.orElseGet { currentUserService.getCurrentUser() }
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

