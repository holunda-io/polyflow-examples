package io.holunda.polyflow.example.process.approval

import mu.KLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class TestAggregate {

  @AggregateIdentifier
  private lateinit var id: String

  companion object: KLogging() {
    @JvmStatic
    @CommandHandler
    @CommandDispatch(localOnly = true)
    fun create(cmd: TestAggregateCreateCommand) = TestAggregate().apply {
      AggregateLifecycle.apply(TestAggregateCreatedEvent(cmd.id))
    }
  }

  @EventSourcingHandler
  fun on(e: TestAggregateCreatedEvent) {
    this.id = e.id
    logger.error { "Test aggregate created with id $id" }
  }
}

data class TestAggregateCreateCommand(
  @TargetAggregateIdentifier
  val id: String
)

data class TestAggregateCreatedEvent(
  val id: String
)
