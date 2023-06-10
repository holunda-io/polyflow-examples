package io.holunda.polyflow.example.process.approval.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.axonframework.common.AxonConfigurationException
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.axonframework.extensions.kafka.KafkaProperties
import org.axonframework.extensions.kafka.autoconfig.KafkaAutoConfiguration
import org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaEventPublisher
import org.axonframework.extensions.kafka.eventhandling.producer.KafkaPublisher
import org.axonframework.extensions.kafka.eventhandling.producer.ProducerFactory
import org.axonframework.serialization.Serializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.*

/**
 * Configure to send polyflow events only if kafka is not disabled (is enabled).
 */
@Configuration
@AutoConfigureBefore(
    KafkaAutoConfiguration::class
) // we should run before Axon Kafka autoconfiguration
@EnableConfigurationProperties(PolyflowAxonKafkaProperties::class)
class PolyflowAxonKafkaConfiguration {
    @ConditionalOnMissingBean
    @Bean
    fun kafkaTopicRouter(properties: PolyflowAxonKafkaProperties): KafkaTopicRouter {
        return KafkaTopicRouter { payloadType ->
          properties.topics.firstOrNull { it.payloadType.isAssignableFrom(payloadType) }?.topic
        }
    }

    @Bean
    @Primary
    fun routingKafkaMessageConverter(
        @Qualifier("eventSerializer") eventSerializer: Serializer,
        kafkaTopicRouter: KafkaTopicRouter
    ): KafkaMessageConverter<String, ByteArray> {
        val defaultConverter: KafkaMessageConverter<String, ByteArray> =
            DefaultKafkaMessageConverter.builder().serializer(eventSerializer).build()
        return object : KafkaMessageConverter<String, ByteArray> {
            override fun createKafkaMessage(
              eventMessage: EventMessage<*>,
              topic: String
            ): ProducerRecord<String, ByteArray> {
                val topicOverride = kafkaTopicRouter.topicForPayloadType(eventMessage.getPayloadType())
                return defaultConverter.createKafkaMessage(eventMessage, topicOverride ?: topic)
            }

            override fun readKafkaMessage(consumerRecord: ConsumerRecord<String, ByteArray>): Optional<EventMessage<*>> {
                return defaultConverter.readKafkaMessage(consumerRecord)
            }
        }
    }

    /**
     * Configures a KafkaEventPublisher that sends events to Kafka only if they are routed via kafka event router.
     *
     * @see KafkaAutoConfiguration.kafkaEventPublisher
     */
    @Bean
    fun routingKafkaEventPublisher(
      kafkaPublisher: KafkaPublisher<String, ByteArray>,
      kafkaProperties: KafkaProperties,
      eventProcessingConfigurer: EventProcessingConfigurer,
      kafkaTopicRouter: KafkaTopicRouter
    ): KafkaEventPublisher<String, ByteArray> {
        val kafkaEventPublisher: KafkaEventPublisher<String, ByteArray> =
            RoutingKafkaEventPublisher.builder<String, ByteArray>()
                .kafkaPublisher(kafkaPublisher)
                .kafkaTopicRouter(kafkaTopicRouter)
                .build()

        /*
         * Register an invocation error handler which re-throws any exception.
         * This will ensure a TrackingEventProcessor to enter the error mode which will retry, and it will ensure the
         * SubscribingEventProcessor to bubble the exception to the callee. For more information see
         *  https://docs.axoniq.io/reference-guide/configuring-infrastructure-components/event-processing/event-processors#error-handling
         */
        // TODO: Check if this still works. Our publisher is no longer in the default processing group, I think.
        eventProcessingConfigurer.registerEventHandler { kafkaEventPublisher }
          .registerListenerInvocationErrorHandler(
                KafkaEventPublisher.DEFAULT_PROCESSING_GROUP
          ) { PropagatingErrorHandler.instance() }
          .assignHandlerTypesMatching(
                KafkaEventPublisher.DEFAULT_PROCESSING_GROUP
          ) { clazz: Class<*> ->
            clazz.isAssignableFrom(
              KafkaEventPublisher::class.java
            )
          }
      when (val processorMode: KafkaProperties.EventProcessorMode = kafkaProperties.producer.eventProcessorMode) {
          KafkaProperties.EventProcessorMode.SUBSCRIBING -> eventProcessingConfigurer.registerSubscribingEventProcessor(KafkaEventPublisher.DEFAULT_PROCESSING_GROUP)
          KafkaProperties.EventProcessorMode.TRACKING -> eventProcessingConfigurer.registerTrackingEventProcessor(KafkaEventPublisher.DEFAULT_PROCESSING_GROUP)
          KafkaProperties.EventProcessorMode.POOLED_STREAMING -> eventProcessingConfigurer.registerPooledStreamingEventProcessor(KafkaEventPublisher.DEFAULT_PROCESSING_GROUP)
          else -> throw AxonConfigurationException("Unknown Event Processor Mode [$processorMode] detected")
      }

      return kafkaEventPublisher
    }

    // We need to duplicate the bean factory from KafkaAutoConfiguration because there is no way to set `publisherAckTimeout` via configuration properties
    @Bean(destroyMethod = "shutDown")
    fun kafkaAcknowledgingPublisher(
      kafkaProducerFactory: ProducerFactory<String, ByteArray>,
      kafkaMessageConverter: KafkaMessageConverter<String, ByteArray>,
      configuration: org.axonframework.config.Configuration,
      properties: KafkaProperties,
      serializer: Serializer
    ): KafkaPublisher<String, ByteArray> {
        return KafkaPublisher
            .builder<String, ByteArray>()
            .producerFactory(kafkaProducerFactory)
            .messageConverter(kafkaMessageConverter)
            .messageMonitor(configuration.messageMonitor(KafkaPublisher::class.java, "kafkaPublisher"))
            .topicResolver { Optional.of(properties.defaultTopic) }
          .serializer(serializer)
            .publisherAckTimeout(
                properties.producer.properties.getOrDefault("delivery.timeout.ms", "30000").toLong() + 1000
            )
            .build()
    }
}
