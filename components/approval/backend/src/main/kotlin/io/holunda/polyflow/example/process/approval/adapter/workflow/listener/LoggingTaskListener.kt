package io.holunda.polyflow.example.process.approval.adapter.workflow.listener

import io.holunda.polyflow.taskpool.collector.task.TaskEventCollectorService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateTask
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class LoggingTaskListener {

  @EventListener(condition = "#task.eventName.equals('create')")
  @Order(TaskEventCollectorService.ORDER - 9)
  fun logTaskCreation(task: DelegateTask) {
    logger.debug { "Created task ${task.id} of type ${task.taskDefinitionKey}" }
  }

}
