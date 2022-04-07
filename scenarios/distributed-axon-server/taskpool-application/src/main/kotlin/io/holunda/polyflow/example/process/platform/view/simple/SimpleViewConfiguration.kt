package io.holunda.polyflow.example.process.platform.view.simple

import io.holunda.polyflow.view.simple.EnablePolyflowSimpleView
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!mongo")
@EnablePolyflowSimpleView
class SimpleViewConfiguration
