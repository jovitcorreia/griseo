package com.gristerm.app.data

import com.gristerm.app.domain.NotificationContract
import com.gristerm.app.domain.RoleType
import java.time.LocalDateTime
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class LoginRequest(@field:NotEmpty val username: String, @field:NotEmpty val password: String)

data class FriendResponse(val id: String, val username: String, val isOnline: Boolean = false)

data class PromotionRequest(
    @field:NotEmpty val id: String,
    @field:NotNull val roles: Set<RoleType>
)

data class SignUpRequest(
    @field:Size(min = 4, max = 16) val username: String,
    @field:Size(min = 8, max = 512) val password: String,
    @field:Email val email: String
)

data class TokenResponse(
    val token: String,
    val type: String = "Bearer",
    val issuedAt: LocalDateTime = LocalDateTime.now()
)

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
