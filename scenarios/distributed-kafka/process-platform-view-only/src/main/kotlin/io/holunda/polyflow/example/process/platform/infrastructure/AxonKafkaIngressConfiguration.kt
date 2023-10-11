package io.holunda.polyflow.example.process.platform.infrastructure

import org.axonframework.extensions.kafka.KafkaProperties
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter
import org.axonframework.extensions.kafka.eventhandling.consumer.ConsumerFactory
import org.axonframework.extensions.kafka.eventhandling.consumer.DefaultConsumerFactory
import org.axonframework.extensions.kafka.eventhandling.consumer.Fetcher
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.KafkaEventMessage
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.SortedKafkaMessageBuffer
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource
import org.axonframework.serialization.Serializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(value = ["polyflow.axon.kafka.enabled"], havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(
    AxonKafkaExtendedProperties::class
)
class AxonKafkaIngressConfiguration(
  @Value("\${HOSTNAME:localhost}") val hostname: String
) {
    /**
     * Consumer factory for tasks.
     *
     * @param properties kafka properties
     * @return consumer factory.
     */
    @Bean
    @Qualifier("polyflowTask")
    fun kafkaConsumerFactoryPolyflowTask(properties: KafkaProperties): ConsumerFactory<String, ByteArray> {
        properties.clientId = "polyflow-task-$hostname"
        return DefaultConsumerFactory<String, ByteArray>(properties.buildConsumerProperties())
    }

    /**
     * Consumer factory for data entries.
     *
     * @param properties kafka properties
     * @return consumer factory.
     */
    @Bean
    @Qualifier("polyflowData")
    fun kafkaConsumerFactoryPolyflowData(properties: KafkaProperties): ConsumerFactory<String, ByteArray> {
        properties.clientId = "polyflow-data-$hostname"
        return DefaultConsumerFactory<String, ByteArray>(properties.buildConsumerProperties())
    }

    /**
     * Creates a streamable kafka message source.
     * The name of this bean is referenced in the application.yaml.
     *
     * @param kafkaProperties      standard kafka properties.
     * @param extendedProperties   extended properties for Polyflow.
     * @param kafkaConsumerFactory consumer factory.
     * @param kafkaFetcher         fetcher instance.
     * @param serializer           serializer.
     * @param meterRegistry        meter registry.
     * @return streaming source.
     */
    @Bean("kafkaMessageSourcePolyflowData")
    @ConditionalOnProperty(value = ["axon.kafka.consumer.event-processor-mode"], havingValue = "TRACKING")
    fun kafkaMessageSourcePolyflowData(
      kafkaProperties: KafkaProperties,
      extendedProperties: AxonKafkaExtendedProperties,
      @Qualifier("polyflowData") kafkaConsumerFactory: ConsumerFactory<String, ByteArray>,
      kafkaFetcher: Fetcher<String, ByteArray, KafkaEventMessage>,
      @Qualifier("eventSerializer") serializer: Serializer,
      messageConverter: KafkaMessageConverter<String, ByteArray>
    ): StreamableKafkaMessageSource<String, ByteArray> {
        return StreamableKafkaMessageSource
            .builder<String, ByteArray>()
            .topics(listOf(extendedProperties.topicDataEntries))
            .consumerFactory(kafkaConsumerFactory)
            .serializer(serializer)
            .fetcher(kafkaFetcher)
            .messageConverter(messageConverter)
            .bufferFactory {
              SortedKafkaMessageBuffer(
                kafkaProperties.fetcher.bufferSize
              )
            }
          .build()
    }

    /**
     * Creates a streamable kafka message source.
     * The name of this bean is referenced in the application.yaml.
     *
     * @param kafkaProperties      standard kafka properties.
     * @param extendedProperties   extended properties for Polyflow.
     * @param kafkaConsumerFactory consumer factory.
     * @param kafkaFetcher         fetcher instance.
     * @param serializer           serializer.
     * @param meterRegistry        meter registry.
     * @return streaming source.
     */
    @Bean("kafkaMessageSourcePolyflowTask")
    @ConditionalOnProperty(value = ["axon.kafka.consumer.event-processor-mode"], havingValue = "TRACKING")
    fun kafkaMessageSourcePolyflowTask(
        kafkaProperties: KafkaProperties,
        extendedProperties: AxonKafkaExtendedProperties,
        @Qualifier("polyflowTask") kafkaConsumerFactory: ConsumerFactory<String, ByteArray>,
        kafkaFetcher: Fetcher<String?, ByteArray, KafkaEventMessage>,
        @Qualifier("eventSerializer") serializer: Serializer,
        messageConverter: KafkaMessageConverter<String, ByteArray>
    ): StreamableKafkaMessageSource<String, ByteArray> {
        return StreamableKafkaMessageSource
            .builder<String, ByteArray>()
            .topics(listOf(extendedProperties.topicTasks))
            .consumerFactory(kafkaConsumerFactory)
            .serializer(serializer)
            .fetcher(kafkaFetcher)
            .messageConverter(messageConverter)
            .bufferFactory {
              SortedKafkaMessageBuffer(
                kafkaProperties.fetcher.bufferSize
              )
            }
          .build()
    }
}
