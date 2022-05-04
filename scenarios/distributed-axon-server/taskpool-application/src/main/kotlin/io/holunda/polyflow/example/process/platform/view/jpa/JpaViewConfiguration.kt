package io.holunda.polyflow.example.process.platform.view.jpa

import io.holunda.polyflow.view.jpa.EnablePolyflowJpaView
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("jpa")
@EnablePolyflowJpaView
class JpaViewConfiguration {
}
