package com.gristerm.app.mapper

import com.gristerm.app.data.TableCreation
import com.gristerm.app.data.TableResponse
import com.gristerm.app.domain.TableModel
import com.gristerm.app.service.UserService
import org.springframework.beans.factory.annotation.Autowired

fun TableCreation.toTableModel(@Autowired userService: UserService) =
    TableModel(
        name = name,
        description = description ?: "",
        cover = cover ?: "",
        narrator = userService.retrieve(narrator).get())

fun TableModel.toTableResponse() =
    TableResponse(
        id,
        name,
        description,
        cover,
        narrator = narrator.toUserResponse(),
        players = players.map { it.toUserResponse() }.toSet(),
        createdDate,
        lastModifiedDate)
