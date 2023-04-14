package io.holunda.polyflow.example.other

import mu.KLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class OtherAggregate {

  @AggregateIdentifier
  private lateinit var id: String

  companion object: KLogging() {
    @JvmStatic
    @CommandHandler
    fun create(cmd: OtherAggregateCreateCommand) = OtherAggregate().apply {
      AggregateLifecycle.apply(OtherAggregateCreatedEvent(cmd.id))
    }
  }

  @EventSourcingHandler
  fun on(e: OtherAggregateCreatedEvent) {
    this.id = e.id
    logger.info { "Test aggregate created with id $id" }
  }
}

