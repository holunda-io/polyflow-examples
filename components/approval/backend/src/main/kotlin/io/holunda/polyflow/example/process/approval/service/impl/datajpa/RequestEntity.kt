package io.holunda.polyflow.example.process.approval.service.impl.datajpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "APP_APPROVAL_REQUEST")
class RequestEntity(
  @Id
  var id: String,
  var subject: String,
  var applicant: String,
  @Column(precision = 19, scale = 2)
  var amount: BigDecimal,
  var currency: String
)
