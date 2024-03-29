package io.holunda.polyflow.example.process.approval.adapter.rest.impl

import io.holunda.polyflow.example.process.approval.adapter.rest.api.EnvironmentApiDelegate
import io.holunda.polyflow.example.process.approval.adapter.rest.model.EnvironmentDto
import io.holunda.polyflow.example.users.SimpleUserService
import io.holunda.polyflow.taskpool.collector.CamundaTaskpoolCollectorProperties
import io.holunda.polyflow.urlresolver.TasklistUrlResolver
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Component

@Component
class EnvironmentResource(
  private val properties: CamundaTaskpoolCollectorProperties,
  private val tasklistUrlResolver: TasklistUrlResolver,
  private val userService: SimpleUserService
) : EnvironmentApiDelegate {

  override fun getEnvironment(): ResponseEntity<EnvironmentDto> =
    ok(
      EnvironmentDto(
        applicationName = properties.applicationName,
        tasklistUrl = tasklistUrlResolver.getTasklistUrl(),
        users = userService.getUserIdentifiers().map { it.key }
      )
    )
}
