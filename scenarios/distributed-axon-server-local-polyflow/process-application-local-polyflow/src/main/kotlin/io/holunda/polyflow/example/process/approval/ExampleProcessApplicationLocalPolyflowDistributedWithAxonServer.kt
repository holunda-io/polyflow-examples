package io.holunda.polyflow.example.process.approval

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import io.holunda.polyflow.bus.jackson.config.FallbackPayloadObjectMapperAutoConfiguration.Companion.PAYLOAD_OBJECT_MAPPER
import io.holunda.polyflow.bus.jackson.configurePolyflowJacksonObjectMapper
import io.holunda.polyflow.datapool.core.EnablePolyflowDataPool
import io.holunda.polyflow.taskpool.core.EnablePolyflowTaskPool
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.deadletter.jpa.DeadLetterEventEntry
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry
import org.axonframework.modelling.saga.repository.jpa.SagaEntry
import org.axonframework.serialization.xml.CompactDriver
import org.axonframework.springboot.util.ConditionalOnMissingQualifiedBean
import org.axonframework.springboot.util.XStreamSecurityTypeUtility
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

/**
 * Starts example application approval process.
 */
fun main(args: Array<String>) {
  SpringApplication.run(ExampleProcessApplicationLocalPolyflowDistributedWithAxonServer::class.java, *args)
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
    DeadLetterEventEntry::class
  ]
)
class ExampleProcessApplicationLocalPolyflowDistributedWithAxonServer {

  @Bean
  fun objectMapper(): ObjectMapper {
    return jacksonObjectMapper()
      .registerModule(JavaTimeModule())
      .configurePolyflowJacksonObjectMapper()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
  }

  @Bean("defaultAxonXStream")
  @ConditionalOnMissingBean
  fun defaultAxonXStream(applicationContext: ApplicationContext): XStream {
    val xStream = XStream(CompactDriver())
    xStream.allowTypesByWildcard(XStreamSecurityTypeUtility.autoConfigBasePackages(applicationContext))
    // This configures XStream to permit any class to be deserialized.
    // FIXME: We might want to make this more restrictive to improve security
    xStream.addPermission(AnyTypePermission.ANY)
    return xStream
  }

  /*
  @Bean
  @Primary
  @ConditionalOnMissingQualifiedBean(beanClass = CommandGateway::class, qualifier = "unqualified")
  fun defaultCommandGateway(bus: CommandBus): CommandGateway = DefaultCommandGateway.builder().commandBus(bus).build()
  */

}
