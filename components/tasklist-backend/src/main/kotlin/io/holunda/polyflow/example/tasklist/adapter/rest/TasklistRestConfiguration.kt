package io.holunda.polyflow.example.tasklist.adapter.rest

import io.holunda.polyflow.view.auth.UnknownUserException
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Configuration
@ControllerAdvice
class TasklistRestConfiguration {

  @ExceptionHandler(UnknownUserException::class)
  fun unknownUserException(e: UnknownUserException) = ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)

  @Bean
  fun tasklistResources(): GroupedOpenApi =
    GroupedOpenApi.builder()
      .group("tasklist")
      .displayName("Task list")
      .addOpenApiCustomiser { openApi ->
        openApi
          .info(
            Info()
              .title("Polyflow Example Process Platform")
              .description("API for the process platform including task list and business data entries list.")
              .version("0.0.1")
          )
      }
      .pathsToMatch(Rest.REQUEST_PATH + "/**")
      .build()

}
