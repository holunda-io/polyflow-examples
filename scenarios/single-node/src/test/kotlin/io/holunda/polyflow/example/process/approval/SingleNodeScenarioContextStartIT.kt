package io.holunda.polyflow.example.process.approval

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("itest", "jpa")
class SingleNodeScenarioContextStartIT {

  @Autowired
  lateinit var userService: SimpleCurrentUserService

  @BeforeEach
  fun `init user`() {
    userService.currentUserStore.username = "kermit"
  }

  @Test
  fun `should start application`() {
    assertThat(userService.getCurrentUser()).isEqualTo("kermit")
  }
}
