package io.holunda.polyflow.example.process.approval.service.impl.datajpa

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
class RequestEntity(
  @Id
  var id: String,
  var subject: String,
  var applicant: String,
  @Column(precision = 19, scale = 2)
  var amount: BigDecimal,
  var currency: String
)
