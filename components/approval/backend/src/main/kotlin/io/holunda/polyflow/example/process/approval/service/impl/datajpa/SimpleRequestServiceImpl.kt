package io.holunda.polyflow.example.process.approval.service.impl.datajpa

import io.holunda.polyflow.example.process.approval.service.Request
import io.holunda.polyflow.example.process.approval.service.RequestService
import org.springframework.stereotype.Component

@Component
class SimpleRequestServiceImpl(
  private val repository: RequestEntityRepository
) : RequestService {
  override fun addRequest(request: Request, username: String, revision: Long): Long {
    repository.save(
      RequestEntity(
        request.id,
        request.subject,
        request.applicant,
        request.amount,
        request.currency
      )
    )
    return revision + 1
  }

  override fun getAllDraftRequests(revision: Long): List<Request> {
    return listOf()
  }

  override fun getAllRequests(revision: Long): List<Request> {
    return repository.findAll().map {
      Request(
        id = it.id,
        applicant = it.applicant,
        subject = it.subject,
        amount = it.amount,
        currency = it.currency
      )
    }
  }

  override fun updateRequest(id: String, request: Request, username: String, revision: Long): Long {
    repository.findById(id).ifPresent {
      repository.save(
        RequestEntity(
          request.id,
          request.subject,
          request.applicant,
          request.amount,
          request.currency
        )
      )
    }
    return revision + 1
  }

  override fun checkRequest(id: String, revision: Long): Boolean {
    return repository.existsById(id)
  }

  override fun getRequest(id: String, revision: Long): Request {
    return repository.findById(id).map {
      Request(
        id = it.id,
        applicant = it.applicant,
        subject = it.subject,
        amount = it.amount,
        currency = it.currency
      )
    }.orElseThrow { IllegalArgumentException("Request with id $id could not be found.") }
  }
}
