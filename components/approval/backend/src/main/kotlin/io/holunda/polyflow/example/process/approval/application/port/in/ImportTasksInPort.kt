package io.holunda.polyflow.example.process.approval.application.port.`in`

/**
 * Use Case:
 * As administrator, I want to import user tasks created previously
 * and make them available in Polyflow.
 */
interface ImportTasksInPort {
  /**
   * Triggers import of existing tasks.
   */
  fun importExistingTasks()
}
