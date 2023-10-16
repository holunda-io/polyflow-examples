package io.holunda.polyflow.example.process.approval.service.impl.datajpa

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

/**
 * Spring Data JPA repository.
 */
interface RequestEntityRepository : PagingAndSortingRepository<RequestEntity, String>, CrudRepository<RequestEntity, String>
