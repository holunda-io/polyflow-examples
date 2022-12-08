package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.api.BusinessDataApiDelegate
import io.holunda.polyflow.example.tasklist.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.rest.model.DataEntryDto
import io.holunda.polyflow.view.DataEntryQueryClient
import io.holunda.polyflow.view.auth.UserService
import io.holunda.polyflow.view.query.data.DataEntriesForUserQuery
import io.holunda.polyflow.view.query.data.DataEntriesQueryResult
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller service data entry items.
 */
@Component
class BusinessDataEntriesResource(
  queryGateway: QueryGateway,
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  private val mapper: TaskWithDataEntriesMapper
) : BusinessDataApiDelegate {

  val businessDataEntriesQueryClient = DataEntryQueryClient(queryGateway)

  override fun getBusinessDataEntries(
    page: Int,
    size: Int,
    sort: String?,
    filters: List<String>,
    xCurrentUserID: String?
  ): ResponseEntity<List<DataEntryDto>> {

    val userIdentifier = xCurrentUserID ?: currentUserService.getCurrentUser()
    val user = userService.getUser(userIdentifier)

    @Suppress("UNCHECKED_CAST")
    val result: DataEntriesQueryResult = businessDataEntriesQueryClient
      .query(
        DataEntriesForUserQuery(
          user = user,
          page = page,
          size = size,
          sort = sort ?: "",
          filters
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

