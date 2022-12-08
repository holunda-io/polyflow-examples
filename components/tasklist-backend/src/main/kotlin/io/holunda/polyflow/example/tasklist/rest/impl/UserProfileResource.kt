package io.holunda.polyflow.example.tasklist.rest.impl

import io.holunda.polyflow.example.tasklist.auth.CurrentUserService
import io.holunda.polyflow.example.tasklist.rest.api.ProfileApiDelegate
import io.holunda.polyflow.example.tasklist.rest.model.UserInfoDto
import io.holunda.polyflow.example.tasklist.rest.model.UserProfileDto
import io.holunda.polyflow.example.users.UserStoreService
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.auth.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class UserProfileResource(
  private val currentUserService: CurrentUserService,
  private val userService: UserService,
  private val userStoreService: UserStoreService
) : ProfileApiDelegate {

  companion object {
    const val HEADER_CURRENT_USER = "X-Current-User-ID"
  }

  override fun getProfile(xCurrentUserID: String?): ResponseEntity<UserProfileDto> {

    val userIdentifier = xCurrentUserID ?: currentUserService.getCurrentUser()
    // retrieve the user
    val user: User = userService.getUser(userIdentifier)

    return ResponseEntity.ok(UserProfileDto(username = user.username))
  }

  override fun getUsers(): ResponseEntity<List<UserInfoDto>> {
    return ResponseEntity.ok(
      userStoreService.getUserIdentifiers().map { UserInfoDto(id = it.key, username = it.value) }
    )
  }


}
