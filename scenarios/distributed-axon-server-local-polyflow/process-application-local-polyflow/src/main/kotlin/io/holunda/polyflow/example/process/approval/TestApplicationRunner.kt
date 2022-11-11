package io.holunda.polyflow.example.process.approval

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestApplicationRunner(
  val commandGateway: CommandGateway
) : ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    commandGateway.sendAndWait<Void>(TestAggregateCreateCommand(UUID.randomUUID().toString()))
  }
}
