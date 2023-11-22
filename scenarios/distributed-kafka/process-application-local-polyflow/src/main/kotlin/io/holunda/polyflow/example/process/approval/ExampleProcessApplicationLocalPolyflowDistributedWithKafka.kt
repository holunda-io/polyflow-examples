package io.holunda.polyflow.example.process.approval

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.holunda.polyflow.bus.jackson.ObjectMapperConfigurationHelper
import io.holunda.polyflow.bus.jackson.config.FallbackPayloadObjectMapperAutoConfiguration
import io.holunda.polyflow.bus.jackson.configurePolyflowJacksonObjectMapper
import io.holunda.polyflow.datapool.core.EnablePolyflowDataPool
import io.holunda.polyflow.example.process.approval.RequestApprovalProcessConfiguration
import io.holunda.polyflow.taskpool.core.EnablePolyflowTaskPool
import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.deadletter.jpa.DeadLetterEventEntry
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine
import org.axonframework.eventsourcing.eventstore.jpa.SnapshotEventEntry
import org.axonframework.modelling.saga.repository.jpa.SagaEntry
import org.axonframework.serialization.Serializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

/**
 * Starts example application approval process.
 */
fun main(args: Array<String>) {
  SpringApplication.run(ExampleProcessApplicationLocalPolyflowDistributedWithKafka::class.java, *args)
}

/**
 * Process application approval only.
 * Includes:
 *  - approval-process-backend
 *  - taskpool-core
 *  - datapool-core
 */
@SpringBootApplication
@EnablePolyflowDataPool
@EnablePolyflowTaskPool
@Import(RequestApprovalProcessConfiguration::class)
@EntityScan(
  basePackageClasses = [
    TokenEntry::class,
    SagaEntry::class,
    DeadLetterEventEntry::class,
    DomainEventEntry::class,
    SnapshotEventEntry::class
  ]
)
class ExampleProcessApplicationLocalPolyflowDistributedWithKafka {

  @Qualifier(FallbackPayloadObjectMapperAutoConfiguration.PAYLOAD_OBJECT_MAPPER)
  @Bean
  @Primary
  fun objectMapper(): ObjectMapper =
    jacksonObjectMapper()
      .registerModule(JavaTimeModule())
      .configurePolyflowJacksonObjectMapper()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  @Bean("defaultAxonObjectMapper")
  @Qualifier("defaultAxonObjectMapper")
  fun defaultAxonObjectMapper(): ObjectMapper =
    jacksonObjectMapper()
      .registerModule(JavaTimeModule())
      .configurePolyflowJacksonObjectMapper()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  @Bean
  fun storageEngine(
    emp: EntityManagerProvider,
    txManager: TransactionManager,
    @Qualifier("eventSerializer")
    eventSerializer: Serializer
  ): EventStorageEngine = JpaEventStorageEngine.builder()
    .entityManagerProvider(emp)
    .eventSerializer(eventSerializer)
    .snapshotSerializer(eventSerializer)
    .transactionManager(txManager)
    .build()

}
