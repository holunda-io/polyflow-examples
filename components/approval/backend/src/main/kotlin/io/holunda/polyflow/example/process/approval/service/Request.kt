package io.holunda.polyflow.example.process.approval.service

import java.math.BigDecimal
import java.util.*

data class Request(
  val id: String = UUID.randomUUID().toString(),
  val applicant: String,
  val subject: String,
  val amount: BigDecimal,
  val currency: String
) {
  override fun toString(): String {
    return "Request(id='$id', applicant='$applicant', subject='$subject', amount=$amount, currency='$currency')"
  }

  fun type(): String = "Approval Request"
  fun description(): String = "Approval request for $subject on behalf of $applicant costing $amount $currency."
  fun name(): String = "$subject for $applicant"
}
