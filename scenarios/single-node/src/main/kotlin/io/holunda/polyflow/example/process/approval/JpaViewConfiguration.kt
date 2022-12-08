package io.holunda.polyflow.example.process.approval


import io.holunda.polyflow.view.jpa.EnablePolyflowJpaView
import mu.KLogging
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry
import org.axonframework.modelling.saga.repository.jpa.SagaEntry
import org.h2.tools.Server
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct


@Configuration
@Profile("jpa")
@EnablePolyflowJpaView
@EntityScan(
  basePackageClasses = [
    DomainEventEntry::class, SagaEntry::class, TokenEntry::class
  ]
)
class JpaViewConfiguration {
  companion object : KLogging()

  @PostConstruct
  fun info() {
    logger.info { "JPA CONFIG STARTED" }
  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  fun inMemoryH2DatabaseServer(): Server {
    return Server.createTcpServer(
      "-tcp", "-tcpAllowOthers", "-tcpPort", "9092"
    )
  }
}
