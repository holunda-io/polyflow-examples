package io.holunda.polyflow.serializer.gdpr

import io.github.oshai.kotlinlogging.KotlinLogging
import org.axonframework.eventhandling.EventMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import java.util.function.BiFunction

private val logger = KotlinLogging.logger {}

class MYMessageInterceptor : MessageDispatchInterceptor<EventMessage<Any>> {

  override fun handle(messages: MutableList<out EventMessage<Any>>): BiFunction<Int, EventMessage<Any>, EventMessage<Any>> {
    return BiFunction { index, event -> event.apply {
      logger.info { "Handling $index, $event" }
    } }
  }
}
