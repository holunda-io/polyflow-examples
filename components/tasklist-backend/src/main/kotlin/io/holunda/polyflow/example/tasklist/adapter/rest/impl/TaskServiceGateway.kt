package io.holunda.polyflow.example.tasklist.adapter.rest.impl

import io.holunda.camunda.taskpool.api.task.InteractionTaskCommand
import io.holunda.polyflow.example.tasklist.adapter.rest.ElementNotFoundException
import io.holunda.polyflow.view.Task
import io.holunda.polyflow.view.TaskQueryClient
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.query.task.TaskForIdQuery
import io.holunda.polyflow.view.query.task.TasksWithDataEntriesForUserQuery
import io.holunda.polyflow.view.query.task.TasksWithDataEntriesQueryResult
import mu.KLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Component

@Component
class TaskServiceGateway(
  queryGateway: QueryGateway,
  val commandGateway: CommandGateway
) {

  companion object : KLogging()

  val taskQueryClient = TaskQueryClient(queryGateway)

  fun send(command: InteractionTaskCommand) {
    commandGateway.send<Any, Any?>(command) { m, r ->
      if (r.isExceptional) {
        logger.error(r.exceptionResult()) { "Error handling submitted command $m" }
      } else {
        logger.debug { "Successfully submitted command $m" }
      }
    }
  }

  fun getTask(id: String): Task = taskQueryClient
    .query(
      TaskForIdQuery(id)
    )
    .join().orElseGet(null) ?: throw ElementNotFoundException()

  fun getTasks(
    user: User,
    page: Int,
    sort: String?,
    size: Int,
    filters: List<String>
  ): TasksWithDataEntriesQueryResult = taskQueryClient
    .query(
      TasksWithDataEntriesForUserQuery(
        user = user,
        assignedToMeOnly = false,
        page = page,
        size = size,
        sort = sort?.let { sortParam -> listOf(sortParam) } ?: listOf(),
        filters = filters
      )
    ).join() ?: throw ElementNotFoundException()
}
