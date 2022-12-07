package io.holunda.polyflow.example.process.approval.rest

import io.holunda.polyflow.example.process.approval.rest.api.EnvironmentApiDelegate
import io.holunda.polyflow.example.process.approval.rest.model.EnvironmentDto
import io.holunda.polyflow.example.users.SimpleUserService
import io.holunda.polyflow.taskpool.collector.CamundaTaskpoolCollectorProperties
import io.holunda.polyflow.urlresolver.TasklistUrlResolver
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Component

@Component
class EnvironmentApiDelegateImpl(
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
