package io.holunda.polyflow.example.process.approval.adapter.rest.impl

import io.holunda.polyflow.example.process.approval.adapter.rest.api.AdministrationApiDelegate
import io.holunda.polyflow.example.process.approval.application.port.`in`.ImportTasksInPort
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.stereotype.Component

@Component
class AdministrationResource(
  private val port: ImportTasksInPort
) : AdministrationApiDelegate {

  override fun triggerTaskImport(): ResponseEntity<Unit> {
    port.importExistingTasks()
    return noContent().build()
  }
}
