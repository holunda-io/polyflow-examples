package io.holunda.polyflow.example.process.approval

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("itest", "jpa")
abstract class AbstractIT {

  @Configuration
  class TestConfiguration {

    @Bean
    fun taskUrlResolver() =
      TestingUrlResolver()
  }
}
