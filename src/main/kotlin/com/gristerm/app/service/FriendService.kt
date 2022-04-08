package com.gristerm.app.service

import com.gristerm.app.domain.FriendInviteNotification
import com.gristerm.app.domain.UserModel
import com.gristerm.app.mapper.toFriendModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendService {
    @Autowired lateinit var userService: UserService

    fun inviteFriend(inviterId: String, invitedId: String) {
        val inviter = userService.retrieveUserById(inviterId).get()
        val invited = userService.retrieveUserById(inviterId).get()
        invited.notifications.add(FriendInviteNotification(inviter.toFriendModel()))
        userService.persistUser(invited)
    }

    fun addFriend(accepterId: String, requesterId: String): UserModel {
        val accepter = userService.retrieveUserById(accepterId).get()
        val requester = userService.retrieveUserById(requesterId).get()
        requester.friends.add(accepter)
        userService.persistUser(requester)
        accepter.friends.add(requester)
        return userService.persistUser(accepter)
    }
}
