package io.holunda.polyflow.example.process.platform.infrastructure

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "polyflow.axon.kafka")
data class AxonKafkaExtendedProperties(
  val enabled: Boolean = true,
  val topicTasks: String,
  val topicDataEntries: String
)
