package io.holunda.polyflow.example.process.platform.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "polyflow.axon.kafka")
@ConstructorBinding
data class AxonKafkaExtendedProperties(
  val enabled: Boolean = true,
  val topicTasks: String,
  val topicDataEntries: String
)
