package com.gristerm.app.controller

import com.gristerm.app.data.UserResponse
import com.gristerm.app.domain.UserCredentials
import com.gristerm.app.domain.UserModel
import com.gristerm.app.mapper.toUserResponse
import com.gristerm.app.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/users")
class UserController(@Autowired val userService: UserService) {
  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MOD')")
  fun indexUsers(
      authentication: Authentication,
      @PageableDefault(sort = ["createdDate"], direction = Sort.Direction.ASC) pageable: Pageable
  ): ResponseEntity<Page<UserResponse>> {
    val userPage: Page<UserModel> = userService.indexUsers(pageable)
    return ResponseEntity.status(CREATED).body(userPage.map { it.toUserResponse() })
  }

  @GetMapping("/{id}")
  fun retrieveUserById(@PathVariable id: String): ResponseEntity<UserResponse> {
    val userModel: UserModel = userService.retrieveUserById(id).get()
    return ResponseEntity.status(OK).body(userModel.toUserResponse())
  }

  @DeleteMapping
  @PreAuthorize("hasAnyRole('USER')")
  fun deleteUserById(authentication: Authentication): ResponseEntity<String> {
    val userCredentials: UserCredentials = authentication.principal as UserCredentials
    userService.deleteUserById(userCredentials.id)
    return ResponseEntity.status(OK).body("${userCredentials.id} deleted successfully")
  }
}
