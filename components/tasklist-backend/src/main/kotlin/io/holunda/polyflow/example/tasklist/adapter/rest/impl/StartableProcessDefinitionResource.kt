package io.holunda.polyflow.example.tasklist.adapter.rest.impl

import io.holunda.polyflow.example.tasklist.adapter.rest.api.ProcessApiDelegate
import io.holunda.polyflow.example.tasklist.adapter.rest.mapper.ProcessDefinitionMapper
import io.holunda.polyflow.example.tasklist.adapter.rest.model.ProcessDefinitionDto
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
  private val userService: UserService,
  queryGateway: QueryGateway,
  private val mapper: ProcessDefinitionMapper
) : ProcessApiDelegate {

  val processDefinitionQueryClient: ProcessDefinitionQueryClient = ProcessDefinitionQueryClient(queryGateway)

  override fun getStartableProcesses(
    xCurrentUserID: String
  ): ResponseEntity<List<ProcessDefinitionDto>> {

    val user = userService.getUser(xCurrentUserID)
    val result = processDefinitionQueryClient.query(ProcessDefinitionsStartableByUserQuery(user = user)).join()

    return ok()
      .body(result.map { mapper.dto(it) })
  }
}
