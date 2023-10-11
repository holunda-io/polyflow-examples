package io.holunda.polyflow.example.process.approval.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "polyflow.axon.kafka")
@ConstructorBinding
data class PolyflowAxonKafkaProperties(
    /**
     * List of mappings of payload class to kafka topic name that payload of this class should be directed to.
     */
    @NestedConfigurationProperty
    val topics: List<PayloadTypeToTopic>
) {
  @ConstructorBinding
  data class PayloadTypeToTopic(
    val payloadType: Class<*>,
    val topic: String
  )
}
