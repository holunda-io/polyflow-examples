package io.holunda.polyflow.example.process.approval.application.usecase

import io.holunda.polyflow.example.process.approval.application.port.`in`.ImportTasksInPort
import io.holunda.polyflow.taskpool.collector.task.TaskServiceCollectorService
import org.springframework.stereotype.Component

@Component
class ImportTasksUseCase(
  private val taskServiceCollectorService: TaskServiceCollectorService
) : ImportTasksInPort {

  override fun importExistingTasks() {
    taskServiceCollectorService.collectAndPopulateExistingTasks(false, 0, 1000)
  }
}
