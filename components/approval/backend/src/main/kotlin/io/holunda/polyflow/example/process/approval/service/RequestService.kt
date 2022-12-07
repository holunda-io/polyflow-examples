package io.holunda.polyflow.example.process.approval.service

/**
 * Service for working with request implementing request use cases.
 */
interface RequestService {

  fun addRequest(request: Request, username: String, revision: Long): Long
  fun getAllDraftRequests(revision: Long): List<Request>
  fun getAllRequests(revision: Long): List<Request>
  fun updateRequest(id: String, request: Request, username: String, revision: Long): Long
  fun checkRequest(id: String, revision: Long): Boolean
  fun getRequest(id: String, revision: Long): Request
}
