package io.holunda.polyflow.example.tasklist.rest.impl

import io.holixon.axon.gateway.query.QueryResponseMessageResponseType
import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.Rest
import io.holunda.polyflow.example.tasklist.rest.api.BusinessDataApi
import io.holunda.polyflow.example.tasklist.rest.impl.UserProfileResource.Companion.HEADER_CURRENT_USER
import io.holunda.polyflow.example.tasklist.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.rest.model.DataEntryDto
import io.holunda.polyflow.view.DataEntryQueryClient
import io.holunda.polyflow.view.auth.UserService
import io.holunda.polyflow.view.query.data.DataEntriesForUserQuery
import io.holunda.polyflow.view.query.data.DataEntriesQueryResult
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller service data entry items.
 */
@RestController
@CrossOrigin
@RequestMapping(Rest.REQUEST_PATH)
class BusinessDataEntriesResource(
  queryGateway: QueryGateway,
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  private val mapper: TaskWithDataEntriesMapper
) : BusinessDataApi {

  val businessDataEntriesQueryClient = DataEntryQueryClient(queryGateway)

  override fun getBusinessDataEntries(
    @RequestParam(value = "page") page: Optional<Int>,
    @RequestParam(value = "size") size: Optional<Int>,
    @RequestParam(value = "sort") sort: Optional<String>,
    @RequestParam(value = "filter") filters: Optional<List<String>>,
    @RequestHeader(value = HEADER_CURRENT_USER, required = false) xCurrentUserID: Optional<String>
  ): ResponseEntity<List<DataEntryDto>> {

    val userIdentifier = xCurrentUserID.orElseGet { currentUserService.getCurrentUser() }
    val user = userService.getUser(userIdentifier)

    @Suppress("UNCHECKED_CAST")
    val result: DataEntriesQueryResult = businessDataEntriesQueryClient
      .query(
        DataEntriesForUserQuery(
          user = user,
          page = page.orElse(1),
          size = size.orElse(Int.MAX_VALUE),
          sort = sort.orElseGet { "" },
          filters = filters.orElseGet { listOf() }
        )
      )
      .join()

    val responseHeaders = HttpHeaders().apply {
      this[TaskResource.HEADER_ELEMENT_COUNT] = result.totalElementCount.toString()
    }

    return ResponseEntity.ok()
      .headers(responseHeaders)
      .body(result.elements.map { mapper.dto(it) })
  }
}

