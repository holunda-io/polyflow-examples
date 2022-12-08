package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.camunda.taskpool.api.task.InteractionTaskCommand
import io.holunda.polyflow.example.tasklist.rest.ElementNotFoundException
import io.holunda.polyflow.view.Task
import io.holunda.polyflow.view.TaskQueryClient
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.query.task.TaskForIdQuery
import io.holunda.polyflow.view.query.task.TasksWithDataEntriesForUserQuery
import io.holunda.polyflow.view.query.task.TasksWithDataEntriesQueryResult
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class TaskServiceGateway(
  queryGateway: QueryGateway,
  val commandGateway: CommandGateway
) {

  val taskQueryClient = TaskQueryClient(queryGateway)

  fun send(command: InteractionTaskCommand) {
    commandGateway.send<Any, Any?>(command) { m, r -> TaskResource.logger.debug("Successfully submitted command $m, $r") }
  }

  fun getTask(id: String): Task = taskQueryClient.query(TaskForIdQuery(id)).join().orElseGet(null) ?: throw ElementNotFoundException()

  fun getTasks(
    user: User,
    page: Int,
    sort: String?,
    size: Int,
    filters: List<String>
  ): TasksWithDataEntriesQueryResult {
    @Suppress("UNCHECKED_CAST")
    return taskQueryClient
      .query(
        TasksWithDataEntriesForUserQuery(
          user = user,
          page = page,
          size = size,
          sort = sort ?: "",
          filters = filters
        )
      )
      .join() ?: throw ElementNotFoundException()
  }
}
