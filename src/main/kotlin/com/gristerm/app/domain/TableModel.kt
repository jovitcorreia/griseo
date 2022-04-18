package com.gristerm.app.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tables")
data class TableModel(
    var name: String,
    var description: String,
    var cover: String,
    @DBRef @Indexed var narrator: UserModel,
    @DBRef @Indexed var players: Set<UserModel> = HashSet(),
    var canvas: Set<CanvasModel> = HashSet(),
    var actors: Set<ActorModel> = HashSet(),
    var handouts: Set<HandoutModel> = HashSet(),
    var messages: Set<MessageModel> = HashSet(),
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
    val createdDate: LocalDateTime = LocalDateTime.now()
) {
  @Id lateinit var id: String

  @LastModifiedDate
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
  lateinit var lastModifiedDate: LocalDateTime
}
