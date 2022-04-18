package com.gristerm.app.service

import com.gristerm.app.data.PromotionRequest
import com.gristerm.app.domain.UserModel
import com.gristerm.app.repository.UserRepository
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired val userRepository: UserRepository) {
  fun delete(id: String) {
    if (userRepository.existsById(id)) {
      return userRepository.deleteById(id)
    }
    throw NoSuchElementException()
  }

  fun index(pageable: Pageable): Page<UserModel> {
    return userRepository.findAll(pageable)
  }

  fun persist(userModel: UserModel): UserModel {
    return userRepository.save(userModel)
  }

  fun promote(promotionRequest: PromotionRequest): UserModel {
    val userModel = retrieve(promotionRequest.id).get()
    userModel.roles.addAll(promotionRequest.roles)
    return userRepository.save(userModel)
  }

  fun retrieve(id: String): Optional<UserModel> {
    return userRepository.findById(id)
  }
}
