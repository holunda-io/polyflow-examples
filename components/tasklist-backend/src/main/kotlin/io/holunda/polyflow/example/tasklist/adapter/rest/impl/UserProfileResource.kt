package io.holunda.polyflow.example.tasklist.adapter.rest.impl

import io.holunda.polyflow.example.tasklist.adapter.rest.api.ProfileApiDelegate
import io.holunda.polyflow.example.tasklist.adapter.rest.model.UserInfoDto
import io.holunda.polyflow.example.tasklist.adapter.rest.model.UserProfileDto
import io.holunda.polyflow.example.users.UserStoreService
import io.holunda.polyflow.view.auth.User
import io.holunda.polyflow.view.auth.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class UserProfileResource(
  private val userService: UserService,
  private val userStoreService: UserStoreService
) : ProfileApiDelegate {

  override fun getProfile(xCurrentUserID: String): ResponseEntity<UserProfileDto> {

    // retrieve the user
    val user: User = userService.getUser(xCurrentUserID)
    return ResponseEntity.ok(UserProfileDto(username = user.username))
  }

  override fun getUsers(): ResponseEntity<List<UserInfoDto>> {
    return ResponseEntity.ok(
      userStoreService.getUserIdentifiers().map { UserInfoDto(id = it.key, username = it.value) }
    )
  }


}
