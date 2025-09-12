package io.holunda.polyflow.example.process.approval

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Tracing.StartOptions
import com.microsoft.playwright.Tracing.StopOptions
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.AriaRole
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import java.nio.file.Paths


@UsePlaywright
class FrontendIT: AbstractIT() {

  @LocalServerPort
  private var port: Int = 0

  @BeforeEach
  fun setUp(context: BrowserContext) {
    // Start tracing before creating / navigating a page.
    context.tracing().start(
      StartOptions()
        .setScreenshots(true)
        .setSnapshots(true)
        .setSources(true)
    )
  }

  @AfterEach
  fun tearDown(context: BrowserContext) {
    // Stop tracing and export it into a zip archive.
    context.tracing().stop(
      StopOptions()
        .setPath(Paths.get("trace.zip"))
    )
  }

  @Test
  fun `should start application`(page: Page) {

    page.navigate("http://localhost:${port}/polyflow")

    // SPA is being served correctly
    assertThat(page).hasTitle("POLYFLOW Process Platform")

    // process definitions are being shown
    val startNewProcessButton = page.getByText("Start Process...")
    assertThat(startNewProcessButton).hasRole(AriaRole.BUTTON)
    startNewProcessButton.click()
    val processList = page.getByLabel("Startable Processes Models")
    assertThat(processList).isInViewport()
    assertThat(processList.getByText("Request Approval")).isInViewport()

    // change user to see tasks on the default started process
    page.getByText("Change User").click()
    page.getByLabel("Available Users").getByText("fozzy").click()
    assertThat(page.getByRole(AriaRole.BUTTON, Page.GetByRoleOptions().setName("current user:"))).hasText("Fozzy");

    // there is a task in the list
    val taskList = page.getByLabel("Open Tasks")
    assertThat(taskList).hasText("Please approve request .* from kermit on behalf of piggy".toPattern())
  }
}
