package com.gristerm.app.controller

import com.gristerm.app.data.UserResponse
import com.gristerm.app.mapper.toUserResponse
import com.gristerm.app.service.FriendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RequestMapping("/friend")
@RestController
class FriendController(@Autowired val friendService: FriendService) {
  @PostMapping("/accept/{acceptedId}")
  @PreAuthorize("hasAnyRole('USER')")
  fun accept(
      authentication: Authentication,
      @PathVariable acceptedId: String
  ): ResponseEntity<UserResponse> {
    val accepterId = authentication.principal.toString()
    return ResponseEntity.status(OK)
        .body(friendService.accept(accepterId, acceptedId).toUserResponse())
  }

  @PostMapping("/invite/{invitedId}")
  @PreAuthorize("hasAnyRole('USER')")
  fun invite(
      authentication: Authentication,
      @PathVariable invitedId: String
  ): ResponseEntity<String> {
    val requesterId = authentication.principal.toString()
    friendService.invite(requesterId, invitedId)
    return ResponseEntity.status(OK).body("friend invitation sent successfully")
  }
}
