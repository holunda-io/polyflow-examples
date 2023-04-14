package io.holunda.polyflow.example.other

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

/**
 * Example application runner sending commands using Axon Framework and sending data entry changes on application start
 */
@Component
class OtherDataEntryApplicationRunner(
  val commandGateway: CommandGateway,
  val dataEntryCommandSender: DataEntryCommandSender
) : ApplicationRunner {

  companion object {
    val VAR = stringVariable("var")
  }

  override fun run(args: ApplicationArguments) {
    commandGateway.sendAndWait<Void>(OtherAggregateCreateCommand(UUID.randomUUID().toString()))

    dataEntryCommandSender.sendDataEntryChange(
      entryId = UUID.randomUUID().toString(),
      entryType = "example.OtherDataEntry",
      payload = builder().set(VAR, "value").build(),
      type = "Other Data Entry",
      state = ProcessingType.IN_PROGRESS.of("processing"),
      authorizationChanges = listOf(AuthorizationChange.addUser("kermit"))
    )
  }
}
