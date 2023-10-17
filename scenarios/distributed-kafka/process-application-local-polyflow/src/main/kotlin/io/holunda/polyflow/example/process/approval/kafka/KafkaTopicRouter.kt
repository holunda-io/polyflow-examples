package io.holunda.polyflow.example.process.approval.kafka

import jakarta.validation.constraints.NotNull

/**
 * Router to decide where to publish events to.
 */
fun interface KafkaTopicRouter {
    /**
     * Retrieves the topic name for given payload type.
     *
     * @param payloadType payload type.
     * @return topic or null, if the event should be dropped.
     */
    fun topicForPayloadType(payloadType: @NotNull Class<*>): String?
}
