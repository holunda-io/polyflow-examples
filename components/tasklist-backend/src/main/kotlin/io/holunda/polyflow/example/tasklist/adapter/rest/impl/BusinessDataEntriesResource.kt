package io.holunda.polyflow.example.tasklist.adapter.rest.impl

import io.holunda.polyflow.example.tasklist.adapter.rest.api.BusinessDataApiDelegate
import io.holunda.polyflow.example.tasklist.adapter.rest.mapper.TaskWithDataEntriesMapper
import io.holunda.polyflow.example.tasklist.adapter.rest.model.DataEntryDto
import io.holunda.polyflow.view.DataEntryQueryClient
import io.holunda.polyflow.view.auth.UserService
import io.holunda.polyflow.view.query.data.DataEntriesForUserQuery
import io.holunda.polyflow.view.query.data.DataEntriesQueryResult
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

/**
 * Controller service data entry items.
 */
@Component
class BusinessDataEntriesResource(
  queryGateway: QueryGateway,
  private val userService: UserService,
  private val mapper: TaskWithDataEntriesMapper
) : BusinessDataApiDelegate {

  val businessDataEntriesQueryClient = DataEntryQueryClient(queryGateway)

  override fun getBusinessDataEntries(
    xCurrentUserID: String,
    page: Int,
    size: Int,
    sort: String?,
    filters: List<String>
  ): ResponseEntity<List<DataEntryDto>> {

    val user = userService.getUser(xCurrentUserID)

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

