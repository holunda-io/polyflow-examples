package io.holunda.polyflow.example.process.approval.adapter.rest

import io.holunda.polyflow.view.auth.UnknownUserException
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@Configuration
@ControllerAdvice
class RequestApprovalProcessRestConfiguration {


  @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Unknown user.")
  @ExceptionHandler(value = [UnknownUserException::class])
  fun forbiddenException() = Unit

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Element not found.")
  @ExceptionHandler(value = [NoSuchElementException::class])
  fun notFoundException() = Unit

  @Bean
  fun requestApprovalProcessResources(): GroupedOpenApi =
    GroupedOpenApi.builder()
      .group("request-approval")
      .displayName("Request Approval")
      .pathsToMatch(Rest.REST_PREFIX + "/**")
      .addOpenApiCustomizer { openApi ->
        openApi
          .info(
            Info()
              .title("Example Approval Process REST API")
              .description("API for the example approval process.")
              .version("0.0.1")
          )
      }
      .build()
}
