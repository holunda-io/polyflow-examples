package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.api.ProcessApiDelegate
import io.holunda.polyflow.example.tasklist.rest.mapper.ProcessDefinitionMapper
import io.holunda.polyflow.example.tasklist.rest.model.ProcessDefinitionDto
import io.holunda.polyflow.view.ProcessDefinition
import io.holunda.polyflow.view.ProcessDefinitionQueryClient
import io.holunda.polyflow.view.auth.UserService
import io.holunda.polyflow.view.query.process.ProcessDefinitionsStartableByUserQuery
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Component


@Component
class StartableProcessDefinitionResource(
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  queryGateway: QueryGateway,
  private val mapper: ProcessDefinitionMapper
) : ProcessApiDelegate {

  val processDefinitionQueryClient: ProcessDefinitionQueryClient = ProcessDefinitionQueryClient(queryGateway)

  override fun getStartableProcesses(
    xCurrentUserID: String?
  ): ResponseEntity<List<ProcessDefinitionDto>> {

    val userIdentifier = xCurrentUserID ?: currentUserService.getCurrentUser()
    val user = userService.getUser(userIdentifier)

    val queryResult = processDefinitionQueryClient.query(ProcessDefinitionsStartableByUserQuery(user = user))
    val result: List<ProcessDefinition> = queryResult.join()

    return ok()
      .body(result.map { mapper.dto(it) })

  }
}
