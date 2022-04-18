package com.gristerm.app.domain

import com.gristerm.app.domain.NotificationType.FRIEND_INVITE
import java.time.LocalDateTime

interface NotificationContract {
  val content: String
  var isDecided: Boolean?
  val type: NotificationType
  val sentIn: LocalDateTime
}

enum class NotificationType {
  FRIEND_INVITE,
  TABLE_INVITE
}

data class FriendInviteNotification(
    var friend: FriendModel,
    override val content: String = "${friend.username} sent you a friend request",
    override var isDecided: Boolean? = null,
    override var type: NotificationType = FRIEND_INVITE,
    override var sentIn: LocalDateTime = LocalDateTime.now()
) : NotificationContract
