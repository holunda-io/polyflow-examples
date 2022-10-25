package io.holunda.polyflow.example.process.approval.rest

import com.fasterxml.jackson.databind.ObjectMapper
import io.holixon.axon.gateway.query.QueryResponseMessageResponseType
import io.holixon.axon.gateway.query.RevisionQueryParameters
import io.holunda.polyflow.example.process.approval.process.RequestApprovalProcessBean
import io.holunda.polyflow.example.process.approval.rest.api.RequestApi
import io.holunda.polyflow.example.process.approval.rest.model.ApprovalRequestDraftDto
import io.holunda.polyflow.example.process.approval.rest.model.ApprovalRequestDto
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
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(path = [Rest.REST_PREFIX])
class RequestController(
  private val requestApprovalProcessBean: RequestApprovalProcessBean,
  private val requestService: RequestService,
  private val userService: UserService,
  private val queryGateway: QueryGateway,
  private val objectMapper: ObjectMapper
) : RequestApi {

  companion object: KLogging()


  override fun startNewApproval(
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String,
    @RequestParam(value = "revision", required = false) revision: Optional<String>,
    @RequestBody request: ApprovalRequestDraftDto
  ): ResponseEntity<Void> {

    val revisionNumber = revision.orElseGet { "1" }.toLong()
    val username = userService.getUser(xCurrentUserID).username

    logger.info { "Starting new process by submitting a draft of ${request.subject}." }

    requestApprovalProcessBean.submitDraft(draft(request), username, revisionNumber)

    return noContent().build()
  }


  override fun getApprovalRequest(
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String,
    @PathVariable("id") id: String
  ): ResponseEntity<ApprovalRequestDto> {

    logger.info { "Retrieving a request with id $id." }
    // val username = userService.getUser(xCurrentUserID).username
    val request = requestService.getRequest(id, 1)

    return ok(approvalRequestDto(request))
  }

  override fun updateApprovalRequest(
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String,
    @PathVariable("id") id: String,
    @Valid @RequestBody(required = false) approvalRequestDto: ApprovalRequestDto
  ): ResponseEntity<Void> {

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

  override fun getApprovalForUser(
    @RequestHeader(value = "X-Current-User-ID", required = true) xCurrentUserID: String,
    @RequestParam(value = "revision", required = false) revision: Optional<String>
  ): ResponseEntity<List<ApprovalRequestDto>> {


    val revisionNumber = revision.orElse("1").toLong()
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
