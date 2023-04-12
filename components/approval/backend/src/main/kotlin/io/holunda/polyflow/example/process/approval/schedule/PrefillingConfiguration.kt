package io.holunda.polyflow.example.process.approval.schedule

import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.service.RequestService
import io.holunda.polyflow.example.process.approval.service.BusinessDataEntry
import mu.KLogging
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * This configuration makes sure that some processes are started directly
 * after empty system initialization to demonstrate how the system looks like
 * with some data.
 */
@Component
class PrefillingConfiguration(
  private val processApprovalProcessBean: RequestApprovalProcessBean,
  private val requestService: RequestService
) {

  companion object : KLogging() {
    const val MIN_INSTANCES = 1L
  }

  @EventListener
  fun postDeploy(postDeploy: PostDeployEvent) {
    // only prefill if the system is empty.
    if (processApprovalProcessBean.countInstances() < MIN_INSTANCES) {
      try {
        logger.info { "Minimal process instance threshold ($MIN_INSTANCES) reached, starting a process." }
        // request for salary increase of Miss Piggy
        val username = "kermit"
        val requests = requestService.getAllDraftRequests(0)
        val request = if (requests.isNotEmpty()) {
          requests[0]
        } else {
          BusinessDataEntry.createSalaryRequest().apply {
            requestService.addRequest(this, username, 0)
          }
        }
        processApprovalProcessBean.startProcess(request.id, username)
      } catch (e: Exception) {
        logger.error(e) { "Error starting process" }
      }
    }
  }
}
