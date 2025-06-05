package io.holunda.polyflow.example.other

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

private val logger = KotlinLogging.logger {}

@Aggregate
class OtherAggregate {

  @AggregateIdentifier
  private lateinit var id: String

  companion object {
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

