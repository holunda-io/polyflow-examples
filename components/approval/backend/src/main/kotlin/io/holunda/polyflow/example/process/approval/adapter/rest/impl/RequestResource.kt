package io.holunda.polyflow.example.process.approval.adapter.rest.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.holixon.axon.gateway.query.QueryResponseMessageResponseType
import io.holixon.axon.gateway.query.RevisionQueryParameters
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.adapter.rest.api.RequestApiDelegate
import io.holunda.polyflow.example.process.approval.adapter.rest.approvalRequestDto
import io.holunda.polyflow.example.process.approval.adapter.rest.draft
import io.holunda.polyflow.example.process.approval.adapter.rest.model.ApprovalRequestDraftDto
import io.holunda.polyflow.example.process.approval.adapter.rest.model.ApprovalRequestDto
import io.holunda.polyflow.example.process.approval.adapter.rest.request
import io.holunda.polyflow.example.process.approval.service.Request
import io.holunda.polyflow.example.process.approval.service.RequestService
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.auth.UserService
import io.holunda.polyflow.view.query.data.DataEntriesForUserQuery
import io.holunda.polyflow.view.query.data.DataEntriesQueryResult
import mu.KLogging
import org.axonframework.messaging.GenericMessage
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.stereotype.Component

@Component
class RequestResource(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
  private val requestService: RequestService,
  private val userService: UserService,
  private val queryGateway: QueryGateway,
  private val objectMapper: ObjectMapper
) : RequestApiDelegate {

  companion object : KLogging()


  override fun startNewApproval(
    xCurrentUserID: String,
    approvalRequestDraftDto: ApprovalRequestDraftDto,
    revision: String?
  ): ResponseEntity<Unit> {

    val revisionNumber: Long = (revision ?: "1").toLong()
    val username = userService.getUser(xCurrentUserID).username

    logger.info { "Starting new process by submitting a draft of ${approvalRequestDraftDto.subject}." }

    requestApprovalProcessBean.submitDraft(draft(approvalRequestDraftDto), username, revisionNumber)

    return noContent().build()
  }


  override fun getApprovalRequest(
    xCurrentUserID: String,
    id: String
  ): ResponseEntity<ApprovalRequestDto> {

    logger.info { "Retrieving a request with id $id." }
    // val username = userService.getUser(xCurrentUserID).username
    val request = requestService.getRequest(id, 1)

    return ok(approvalRequestDto(request))
  }

  override fun updateApprovalRequest(
    xCurrentUserID: String,
    id: String,
    approvalRequestDto: ApprovalRequestDto
  ): ResponseEntity<Unit> {

    logger.info { "Modifying a request with id $id." }

    val username = userService.getUser(xCurrentUserID).username
    requestService.updateRequest(
      id = id,
      request = request(approvalRequestDto),
      username = username,
      revision = 1
    )

    return noContent().build()
  }

  override fun getApprovalsForUser(
    xCurrentUserID: String,
    revision: String?
  ): ResponseEntity<List<ApprovalRequestDto>> {


    val revisionNumber = (revision ?: "1").toLong()
    val username = userService.getUser(xCurrentUserID).username

    logger.info { "Retrieving requests visible for $username." }

    val result = queryGateway.query(
      GenericMessage.asMessage(
        DataEntriesForUserQuery(
          user = User(username, setOf()),
          page = 1,
          size = Int.MAX_VALUE,
          sort = "",
          filters = listOf()
        )
      ).andMetaData(RevisionQueryParameters(revisionNumber, 10).toMetaData()),
      QueryResponseMessageResponseType.queryResponseMessageResponseType<DataEntriesQueryResult>()
    ).join()

    return ok(result.elements
      .map {
        objectMapper.convertValue(it.payload, Request::class.java)
      }.map {
        approvalRequestDto(it)
      }
    )
  }
}
