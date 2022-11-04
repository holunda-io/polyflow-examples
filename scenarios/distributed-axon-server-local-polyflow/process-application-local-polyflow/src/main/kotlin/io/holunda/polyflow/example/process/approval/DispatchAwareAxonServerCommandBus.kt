package io.holunda.polyflow.example.process.approval

import io.axoniq.axonserver.grpc.command.Command
import io.axoniq.axonserver.grpc.command.CommandResponse
import mu.KLogging
import org.axonframework.axonserver.connector.AxonServerConfiguration
import org.axonframework.axonserver.connector.AxonServerConnectionManager
import org.axonframework.axonserver.connector.PriorityRunnable
import org.axonframework.axonserver.connector.TargetContextResolver
import org.axonframework.axonserver.connector.command.AxonServerCommandBus
import org.axonframework.axonserver.connector.command.AxonServerRegistration
import org.axonframework.axonserver.connector.command.CommandLoadFactorProvider
import org.axonframework.axonserver.connector.command.CommandPriorityCalculator
import org.axonframework.axonserver.connector.util.ProcessingInstructionHelper
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.distributed.RoutingStrategy
import org.axonframework.common.Registration
import org.axonframework.messaging.MessageHandler
import org.axonframework.serialization.Serializer
import org.axonframework.springboot.autoconfig.AxonServerBusAutoConfiguration
import org.axonframework.springboot.util.ConditionalOnMissingQualifiedBean
import org.axonframework.tracing.SpanFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.util.concurrent.CompletableFuture
import javax.annotation.Nonnull


class DispatchAwareAxonServerCommandBus(builder: Builder) : AxonServerCommandBus(builder) {

  companion object : KLogging()

  override fun subscribe(commandName: String, messageHandler: MessageHandler<in CommandMessage<*>?>): Registration {
    return if (commandName == "io.holunda.polyflow.example.process.approval.TestAggregateCreateCommand") {
      localSubscribe(commandName, messageHandler)
    } else {
      super.subscribe(commandName, messageHandler)
    }
  }

  fun localSubscribe(@Nonnull commandName: String, @Nonnull messageHandler: MessageHandler<in CommandMessage<*>?>): Registration {
    logger.debug(
      "Subscribing command with name [{}] to local CommandBus only.",
      commandName
    )
    val localRegistration = localSegment().subscribe(commandName, messageHandler)
    return AxonServerRegistration(localRegistration) {  }
  }

}


class DispatchAwareAxonServerCommandBusConfiguration : AxonServerBusAutoConfiguration() {

  @Bean
  @Primary
  @ConditionalOnMissingQualifiedBean(qualifier = "!localSegment", beanClass = CommandBus::class)
  override fun axonServerCommandBus(
    axonServerConnectionManager: AxonServerConnectionManager,
    axonServerConfiguration: AxonServerConfiguration,
    @Qualifier("localSegment") localSegment: CommandBus,
    @Qualifier("messageSerializer") messageSerializer: Serializer,
    routingStrategy: RoutingStrategy?,
    priorityCalculator: CommandPriorityCalculator?,
    loadFactorProvider: CommandLoadFactorProvider?,
    targetContextResolver: TargetContextResolver<in CommandMessage<*>?>?,
    spanFactory: SpanFactory?
  ): AxonServerCommandBus? {
    return DispatchAwareAxonServerCommandBus(
      AxonServerCommandBus
        .builder()
        .axonServerConnectionManager(axonServerConnectionManager)
        .configuration(axonServerConfiguration)
        .localSegment(localSegment)
        .serializer(messageSerializer)
        .routingStrategy(routingStrategy)
        .priorityCalculator(priorityCalculator)
        .loadFactorProvider(loadFactorProvider)
        .targetContextResolver(targetContextResolver)
        .spanFactory(spanFactory!!)
    )
  }
}
