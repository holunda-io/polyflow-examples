package io.holunda.polyflow.example.process.approval

import io.holunda.polyflow.urlresolver.TasklistUrlResolver
import io.holunda.polyflow.view.DataEntry
import io.holunda.polyflow.view.FormUrlResolver
import io.holunda.polyflow.view.ProcessDefinition
import io.holunda.polyflow.view.Task
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent
import org.springframework.context.event.EventListener

/**
 * Override config based url resolvers, because the WebEnvironment.RANDOM_PORT cannot be resolved inside configurations.
 */
class TestingUrlResolver : FormUrlResolver, TasklistUrlResolver {

  private var port: Int = 0

  /**
   * The only way to get the random port at runtime.
   * See: https://www.baeldung.com/spring-boot-running-port#2-handling-servletwebserverinitializedevent
   */
  @EventListener
  fun onApplicationEvent(event: ServletWebServerInitializedEvent) {
    port = event.webServer.port
  }

  override fun resolveUrl(task: Task): String {
    return "http://localhost:$port/example-process-approval/tasks/${task.formKey}/${task.id}?userId=%userId%"
  }

  override fun resolveUrl(processDefinition: ProcessDefinition): String {
    return "http://localhost:$port/example-process-approval/${processDefinition.formKey}?userId=%userId%"
  }

  override fun resolveUrl(dataEntry: DataEntry): String {
    return "http://localhost:$port/example-process-approval/approval-request/${dataEntry.entryId}?userId=%userId%"
  }

  override fun getTasklistUrl(): String {
    return "http://localhost:$port/polyflow/tasks"
  }
}
