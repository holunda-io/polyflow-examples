package io.holunda.polyflow.example.process.approval.service

import io.holunda.polyflow.example.process.approval.service.Request
import java.math.BigDecimal
import java.util.*

object BusinessDataEntry {
  const val REQUEST = "io.holunda.camunda.taskpool.example.ApprovalRequest"
  const val USER = "io.holunda.camunda.taskpool.example.User"


  fun createSalaryRequest(id: String = UUID.randomUUID().toString()) = Request(
    id = id,
    subject = "Salary increase",
    amount = BigDecimal(10000),
    currency = "USD",
    applicant = "piggy"
  )

  fun createAdvertisingCampaignRequest(id: String = UUID.randomUUID().toString()) = Request(
    id = id,
    subject = "Small advertising campaign",
    amount = BigDecimal(60000),
    currency = "USD",
    applicant = "ironman"
  )

}
