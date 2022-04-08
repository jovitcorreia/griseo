package com.gristerm.app.mapper

import com.gristerm.app.data.FriendResponse
import com.gristerm.app.data.SignUpRequest
import com.gristerm.app.data.UserResponse
import com.gristerm.app.domain.FriendModel
import com.gristerm.app.domain.UserCredentials
import com.gristerm.app.domain.UserModel

fun SignUpRequest.toUserModel() = UserModel(username = username, email = email, password = password)

fun UserModel.toUserResponse() =
    UserResponse(
        id,
        username,
        email,
        isOnline,
        isActive,
        roles,
        hasUnreadNotifications,
        notifications,
        friends.map { it.toFriendResponse() }.toSet(),
        createdDate,
        lastModifiedDate
    )

fun UserModel.toFriendModel() = FriendModel(id, username)

fun UserModel.toFriendResponse() = FriendResponse(id, username, isOnline)

fun UserModel.toUserCredentials() = UserCredentials(id, username, password, isActive)
