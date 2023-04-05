package io.holunda.polyflow.example.process.approval

/**
 * Hold the username.
 */
data class CurrentUserStore(var username: String? = null) {
  /**
   * Clears the store.
   */
  fun clear() {
    this.username = null
  }
}
