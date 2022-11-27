package io.holunda.polyflow.example.process.approval

import io.holunda.camunda.bpm.data.CamundaBpmData.builder
import io.holunda.camunda.bpm.data.CamundaBpmData.stringVariable
import io.holunda.camunda.taskpool.api.business.AuthorizationChange
import io.holunda.camunda.taskpool.api.business.ProcessingType
import io.holunda.polyflow.datapool.sender.DataEntryCommandSender
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestApplicationRunner(
  val commandGateway: CommandGateway,
  val dataEntryCommandSender: DataEntryCommandSender
) : ApplicationRunner {

  companion object {
    val VAR = stringVariable("var")
  }

  override fun run(args: ApplicationArguments) {
    commandGateway.sendAndWait<Void>(TestAggregateCreateCommand(UUID.randomUUID().toString()))

    dataEntryCommandSender.sendDataEntryChange(
      entryId = UUID.randomUUID().toString(),
      entryType = "test.DataEntry",
      payload = builder().set(VAR, "value").build(),
      type = "Test Data Entry",
      state = ProcessingType.IN_PROGRESS.of("processing"),
      authorizationChanges = listOf(AuthorizationChange.addUser("kermit"))
    )
  }
}
