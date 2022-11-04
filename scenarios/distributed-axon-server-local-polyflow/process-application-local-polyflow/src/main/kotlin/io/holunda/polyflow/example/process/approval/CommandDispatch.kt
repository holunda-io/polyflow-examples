package io.holunda.polyflow.example.process.approval

/**
 * Controls registration of command dispatch for Axon Server Command Bus.
 */
@MustBeDocumented
annotation class CommandDispatch(
  /**
   * Controls remote command handler registration. If set to [true] the handler is registered on local command bus only.
   * Defaults to false.
   */
  val localOnly: Boolean = false
)
