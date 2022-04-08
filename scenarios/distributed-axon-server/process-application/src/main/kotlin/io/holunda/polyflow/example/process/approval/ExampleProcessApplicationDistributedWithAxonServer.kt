package io.holunda.polyflow.example.process.approval

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import io.holunda.polyflow.bus.jackson.config.FallbackPayloadObjectMapperAutoConfiguration.Companion.PAYLOAD_OBJECT_MAPPER
import io.holunda.polyflow.bus.jackson.configurePolyflowJacksonObjectMapper
import org.axonframework.serialization.xml.CompactDriver
import org.axonframework.springboot.util.XStreamSecurityTypeUtility
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

/**
 * Starts example application approval process.
 */
fun main(args: Array<String>) {
  SpringApplication.run(ExampleProcessApplicationDistributedWithAxonServer::class.java, *args)
}

/**
 * Process application approval only.
 * Includes:
 *  - process-backend
 */
@SpringBootApplication
@Import(RequestApprovalProcessConfiguration::class)
class ExampleProcessApplicationDistributedWithAxonServer {

  @Bean
  @Primary
  @Qualifier(PAYLOAD_OBJECT_MAPPER)
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

//  @Bean
//  fun eventSerializer(objectMapper: ObjectMapper): Serializer = JacksonSerializer.builder().objectMapper(objectMapper).build()
//
//  @Bean
//  fun messageSerializer(objectMapper: ObjectMapper): Serializer = JacksonSerializer.builder().objectMapper(objectMapper).build()
}
