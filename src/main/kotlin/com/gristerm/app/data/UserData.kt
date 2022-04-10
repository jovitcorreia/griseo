package com.gristerm.app.data

import com.gristerm.app.domain.NotificationContract
import com.gristerm.app.domain.RoleType
import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.Size

class SignInRequest(val username: String? = null, val password: String? = null)

data class SignUpRequest(
    @field:Size(min = 4, max = 16) val username: String,
    @field:Size(min = 8, max = 512) val password: String,
    @field:Email val email: String
)

data class FriendResponse(val id: String, val username: String, val isOnline: Boolean = false)

data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val isOnline: Boolean,
    val isActive: Boolean,
    val roles: Set<RoleType>,
    val hasUnreadNotifications: Boolean,
    val notifications: Set<NotificationContract>,
    val friends: Set<FriendResponse>,
    val createdDate: LocalDateTime,
    val lastModifiedDate: LocalDateTime
)

data class TokenResponse(
    val token: String,
    val type: String = "Bearer",
    val issuedAt: LocalDateTime = LocalDateTime.now()
)
