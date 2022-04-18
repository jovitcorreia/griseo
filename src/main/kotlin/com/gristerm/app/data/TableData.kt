package com.gristerm.app.data

import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty

data class TableCreation(
    @field:NotEmpty val name: String,
    val description: String? = null,
    val cover: String? = null,
    @field:NotEmpty val narrator: String
)

data class TableResponse(
    val id: String,
    val name: String,
    val description: String?,
    val cover: String?,
    val narrator: UserResponse,
    val players: Set<UserResponse>,
    val createdDate: LocalDateTime,
    val lastModifiedDate: LocalDateTime
)
