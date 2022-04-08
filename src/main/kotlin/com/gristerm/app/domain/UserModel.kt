package com.gristerm.app.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.gristerm.app.domain.RoleType.USER
import java.time.LocalDateTime
import java.util.stream.Collectors
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

enum class RoleType {
  ADMIN,
  MOD,
  USER
}

@CompoundIndex(def = "{'email': 1}", unique = true)
@CompoundIndex(def = "{'username': 1}", unique = true)
@Document(collection = "users")
data class UserModel(
    var username: String,
    val email: String,
    var password: String,
    var isOnline: Boolean = false,
    var isActive: Boolean = true,
    var roles: MutableSet<RoleType> = hashSetOf(USER),
    var hasUnreadNotifications: Boolean = false,
    var notifications: MutableSet<NotificationContract> = HashSet(),
    @DBRef var friends: MutableSet<UserModel> = HashSet(),
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
    val createdDate: LocalDateTime = LocalDateTime.now()
) {
  @Id lateinit var id: String

  @LastModifiedDate
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
  lateinit var lastModifiedDate: LocalDateTime
}

data class UserCredentials(
    var id: String,
    private val username: String,
    private val password: String,
    var isActive: Boolean = true,
    var roles: MutableSet<RoleType> = HashSet()
) : UserDetails {
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return roles.stream().map { SimpleGrantedAuthority(it.name) }.collect(Collectors.toList())
  }

  override fun getPassword(): String {
    return password
  }

  override fun getUsername(): String {
    return username
  }

  override fun isAccountNonExpired(): Boolean {
    return isActive
  }

  override fun isAccountNonLocked(): Boolean {
    return isActive
  }

  override fun isCredentialsNonExpired(): Boolean {
    return isActive
  }

  override fun isEnabled(): Boolean {
    return isActive
  }
}

data class FriendModel(val id: String, val username: String)
