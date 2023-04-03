package io.holunda.polyflow.example.process.approval

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SimpleCurrentUserServiceTest {

  private val currentUserStore = CurrentUserStore()
  private val simpleCurrentUserService = SimpleCurrentUserService(currentUserStore = currentUserStore)

  @Test
  fun `reads current user`() {
    currentUserStore.username = "current"
    assertThat(simpleCurrentUserService.getCurrentUser()).isEqualTo("current")
  }

  @Test
  fun `fails if no current user set`() {

    val e = assertThrows<IllegalArgumentException> { simpleCurrentUserService.getCurrentUser() }
    assertThat(e.message).isEqualTo("No current user set")
  }

}
