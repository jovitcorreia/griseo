package com.gristerm.app.service

import com.gristerm.app.data.TableCreation
import com.gristerm.app.domain.TableModel
import com.gristerm.app.mapper.toTableModel
import com.gristerm.app.repository.TableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TableService {
  @Autowired lateinit var tableRepository: TableRepository
  @Autowired lateinit var userService: UserService

  fun createTable(tableData: TableCreation): TableModel {
    val tableModel = tableData.toTableModel(userService)
    tableModel.lastModifiedDate = tableModel.createdDate
    return tableRepository.save(tableModel)
  }
}
