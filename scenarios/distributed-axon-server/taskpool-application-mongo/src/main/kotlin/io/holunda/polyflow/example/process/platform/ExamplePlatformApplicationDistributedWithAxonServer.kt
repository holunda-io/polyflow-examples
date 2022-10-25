package io.holunda.polyflow.example.process.platform

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import io.holixon.axon.gateway.query.RevisionValue
import io.holunda.polyflow.bus.jackson.config.FallbackPayloadObjectMapperAutoConfiguration.Companion.PAYLOAD_OBJECT_MAPPER
import io.holunda.polyflow.bus.jackson.configurePolyflowJacksonObjectMapper
import io.holunda.polyflow.datapool.core.EnablePolyflowDataPool
import io.holunda.polyflow.example.tasklist.EnableTasklist
import io.holunda.polyflow.example.users.UsersConfiguration
import io.holunda.polyflow.taskpool.core.EnablePolyflowTaskPool
import io.holunda.polyflow.urlresolver.EnablePropertyBasedFormUrlResolver
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.correlation.CorrelationDataProvider
import org.axonframework.messaging.correlation.MessageOriginProvider
import org.axonframework.messaging.correlation.MultiCorrelationDataProvider
import org.axonframework.messaging.correlation.SimpleCorrelationDataProvider
import org.axonframework.serialization.xml.CompactDriver
import org.axonframework.springboot.util.XStreamSecurityTypeUtility
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

/**
 * Starts platform application.
 */
fun main(args: Array<String>) {
  SpringApplication.run(ExamplePlatformApplicationDistributedWithAxonServer::class.java, *args)
}

/**
 * Process application using Axon Server as event store and communication platform.
 * Includes:
 * - core/taskpool
 * - core/datapool
 * - view
 * - tasklist backend
 */
@SpringBootApplication(
  exclude = [
    MongoAutoConfiguration::class,
    MongoReactiveAutoConfiguration::class,
    MongoDataAutoConfiguration::class,
    MongoReactiveDataAutoConfiguration::class
  ]
)
@Import(
  UsersConfiguration::class
)
@EnablePolyflowTaskPool
@EnablePolyflowDataPool
@EnableTasklist
@EnablePropertyBasedFormUrlResolver
class ExamplePlatformApplicationDistributedWithAxonServer {

  @Qualifier(PAYLOAD_OBJECT_MAPPER)
  @Bean
  @Primary
  fun payloadObjectMapper(): ObjectMapper {
    return jacksonObjectMapper()
      .registerModule(JavaTimeModule())
      .configurePolyflowJacksonObjectMapper()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // let the dates be strings and not nanoseconds
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // be nice to properties we don't understand
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

//  @Bean
//  fun eventSerializer(objectMapper: ObjectMapper): Serializer = JacksonSerializer.builder().objectMapper(objectMapper).build()
//
//  @Bean
//  fun messageSerializer(objectMapper: ObjectMapper): Serializer = JacksonSerializer.builder().objectMapper(objectMapper).build()

}


