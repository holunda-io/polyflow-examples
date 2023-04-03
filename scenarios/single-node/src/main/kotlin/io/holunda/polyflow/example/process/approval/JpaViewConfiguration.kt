package io.holunda.polyflow.example.process.approval


import io.holunda.polyflow.view.jpa.EnablePolyflowJpaView
import mu.KLogging
import org.axonframework.eventhandling.deadletter.jpa.DeadLetterEntry
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry
import org.axonframework.modelling.saga.repository.jpa.SagaEntry
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct

@Configuration
@Profile("jpa")
@EnablePolyflowJpaView
@EntityScan(
  basePackageClasses = [
    DomainEventEntry::class, SagaEntry::class, TokenEntry::class, DeadLetterEntry::class
  ]
)
class JpaViewConfiguration {
  companion object: KLogging()
  @PostConstruct
  fun info() {
    logger.info { "JPA CONFIG STARTED"}
  }
}
