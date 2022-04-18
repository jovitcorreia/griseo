package com.gristerm.app.repository

import com.gristerm.app.domain.TableModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository interface TableRepository : MongoRepository<TableModel, String> {}
