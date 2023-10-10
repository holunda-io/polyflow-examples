package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.Rest
import io.holunda.polyflow.example.tasklist.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.rest.model.TaskWithDataEntriesDto
import io.holunda.polyflow.view.TaskWithDataEntries
import io.holunda.polyflow.view.auth.UserService
import io.holunda.polyflow.view.query.task.TasksWithDataEntriesForUserQuery
import io.holunda.polyflow.view.query.task.TasksWithDataEntriesQueryResult
import mu.KLogging
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.SubscriptionQueryResult
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.util.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

/**
 * Reactive controller delivering tasks.
 */
@RestController
@RequestMapping(Rest.REACTIVE_PATH)
class ReactiveTasksController(
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  private val queryGateway: QueryGateway,
  private val mapper: TaskWithDataEntriesMapper
) {

  companion object : KLogging()

  @GetMapping(path = ["/tasks"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE])
  fun getTasks(
    @NotNull @Pattern(regexp = "^([\\w]*)=([.]+)?$") @RequestParam(value = "filter", required = true, defaultValue = "[]") filters: List<String>,
    @RequestParam(value = "page", required = false, defaultValue = "1") page: Optional<Int>,
    @RequestParam(value = "size", required = false, defaultValue = "20") size: Optional<Int>,
    @Pattern(regexp = "^[-+]([\\w]*)$") @RequestParam(value = "sort", required = false, defaultValue = "") sort: Optional<String>,
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: Optional<String>
  ): Flux<TaskWithDataEntriesDto> {

    val userIdentifier = xCurrentUserID.orElseGet { currentUserService.getCurrentUser() }
    val user = userService.getUser(userIdentifier)

    @Suppress("UNCHECKED_CAST")
    val taskEvents: SubscriptionQueryResult<TasksWithDataEntriesQueryResult, TaskWithDataEntries> = queryGateway
      .subscriptionQuery(
        TasksWithDataEntriesForUserQuery(
          user = user,
          page = page.orElse(1),
          size = size.orElse(Int.MAX_VALUE),
          sort = sort.orElseGet { "" },
          filters = filters
        ),
        ResponseTypes.instanceOf(TasksWithDataEntriesQueryResult::class.java),
        ResponseTypes.instanceOf(TaskWithDataEntries::class.java)
      )

    return taskEvents
      .initialResult().flatMapMany { Flux.fromIterable(it.elements) }
      .concatWith(taskEvents.updates())
      .map {
        mapper.dto(it)
      }
  }
}
