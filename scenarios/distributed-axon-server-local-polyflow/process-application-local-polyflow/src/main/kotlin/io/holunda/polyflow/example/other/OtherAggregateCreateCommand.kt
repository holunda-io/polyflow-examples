package io.holunda.polyflow.example.other

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class OtherAggregateCreateCommand(
  @TargetAggregateIdentifier
  val id: String
)
