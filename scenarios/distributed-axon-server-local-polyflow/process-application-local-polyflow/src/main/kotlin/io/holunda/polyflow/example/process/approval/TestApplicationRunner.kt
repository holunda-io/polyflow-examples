package io.holunda.polyflow.example.process.approval

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestApplicationRunner(
  @Qualifier("localSegment")
  val commandBus: CommandBus,
) : ApplicationRunner {

  private val commandGateway = DefaultCommandGateway.builder().commandBus(commandBus).build()

  override fun run(args: ApplicationArguments) {
    commandGateway.sendAndWait<Void>(TestAggregateCreateCommand(UUID.randomUUID().toString()))
  }
}
