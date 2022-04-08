package com.gristerm.app.controller

import com.gristerm.app.data.UserResponse
import com.gristerm.app.mapper.toUserResponse
import com.gristerm.app.service.FriendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
class FriendController(@Autowired val friendService: FriendService) {
  @PostMapping("/invite/{id}")
  fun inviteFriend(@PathVariable id: String): ResponseEntity<String> {
    val requester = "624f880d46abdb741ac6686e"
    friendService.inviteFriend(requester, id)
    return ResponseEntity.status(OK).body("friend invitation sent successfully")
  }

  @PostMapping("/add/{id}")
  fun addFriend(@PathVariable id: String): ResponseEntity<UserResponse> {
    val accepter = "624f880d46abdb741ac6686e"
    return ResponseEntity.status(OK).body(friendService.addFriend(accepter, id).toUserResponse())
  }
}
