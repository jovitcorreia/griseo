package com.gristerm.app.service

import com.gristerm.app.domain.FriendInviteNotification
import com.gristerm.app.domain.UserModel
import com.gristerm.app.mapper.toFriendModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendService {
  @Autowired lateinit var userService: UserService

  fun accept(accepterId: String, requesterId: String): UserModel {
    val accepter = userService.retrieve(accepterId).get()
    val requester = userService.retrieve(requesterId).get()
    requester.friends.add(accepter)
    userService.persist(requester)
    accepter.friends.add(requester)
    return userService.persist(accepter)
  }

  fun invite(inviterId: String, invitedId: String) {
    val inviter = userService.retrieve(inviterId).get()
    val invited = userService.retrieve(invitedId).get()
    invited.notifications.add(FriendInviteNotification(inviter.toFriendModel()))
    userService.persist(invited)
  }
}
