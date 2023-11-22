package io.holunda.polyflow.serializer.gdpr

import mu.KLogging
import org.axonframework.eventhandling.EventMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import java.util.function.BiFunction

class MYMessageInterceptor : MessageDispatchInterceptor<EventMessage<Any>> {

  companion object: KLogging()

  override fun handle(messages: MutableList<out EventMessage<Any>>): BiFunction<Int, EventMessage<Any>, EventMessage<Any>> {
    return BiFunction { index, event -> event.apply {
      logger.info { "Handling $index, $event" }
    } }
  }
}
