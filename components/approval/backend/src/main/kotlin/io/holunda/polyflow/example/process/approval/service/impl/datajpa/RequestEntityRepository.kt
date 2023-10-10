package io.holunda.polyflow.example.process.approval.service.impl.datajpa

import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

/**
 * Spring Data JPA repository.
 */
interface RequestEntityRepository : PagingAndSortingRepository<RequestEntity, String> {

  fun save(requestEntity: RequestEntity): RequestEntity
  fun findAll(): List<RequestEntity>
  fun findById(id: String): Optional<RequestEntity>
  fun existsById(id: String): Boolean
}

