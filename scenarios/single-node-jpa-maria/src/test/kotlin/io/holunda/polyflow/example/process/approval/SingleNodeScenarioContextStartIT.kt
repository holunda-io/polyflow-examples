package io.holunda.polyflow.example.process.approval

import io.holunda.polyflow.view.auth.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("itest", "jpa")
class SingleNodeScenarioContextStartIT {

  @Autowired
  lateinit var userService: UserService

  @Test
  fun `should start application`() {
    assertThat(userService.getUser("37bff195-06fe-4788-9480-a7d9ac6474e1").username).isEqualTo("kermit")
  }
}
